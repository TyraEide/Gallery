import { render, fireEvent, screen, waitFor } from "@testing-library/svelte";
import userEvent from "@testing-library/user-event";
import Register from "../pages/Register.svelte";
import { beforeEach, expect, test, vi } from "vitest";
import '@testing-library/jest-dom';

// Mocking browser-specific methods
vi.mock("../ts_modules/routing", () => ({
    redirect: vi.fn(),
}));

// Mock global fetch
global.fetch = vi.fn();

describe("Register Component", () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    const fillForm = async (username: string, email: string, password: string, confirmPassword: string) => {
        await userEvent.type(screen.getByLabelText("Username"), username);
        await userEvent.type(screen.getByLabelText("Email"), email);
        await userEvent.type(screen.getByLabelText("Password"), password);
        await userEvent.type(screen.getByLabelText("Confirm Password"), confirmPassword);
    };

    const clickRegisterButton = async () => {
        await fireEvent.click(screen.getByRole("button", { name: "Register" }));
    };

    test("renders the register form", () => {
        render(Register);
        expect(screen.getByText("Register User")).toBeInTheDocument();
        expect(screen.getByLabelText("Username")).toBeInTheDocument();
        expect(screen.getByLabelText("Email")).toBeInTheDocument();
        expect(screen.getByLabelText("Password")).toBeInTheDocument();
        expect(screen.getByLabelText("Confirm Password")).toBeInTheDocument();
        expect(screen.getByRole("button", { name: "Register" })).toBeInTheDocument();
    });

    test("shows validation message for short password", async () => {
        render(Register);
        await fillForm("testuser", "test@example.com", "short", "short");
        await clickRegisterButton();

        // Wait for the error message to appear in the document
        await waitFor(() => expect(screen.getByText("Password must be at least 8 characters.")).toBeInTheDocument());
    });

    test("shows validation message for mismatched passwords", async () => {
        render(Register);
        await fillForm("testuser", "test@example.com", "password123", "password321");
        await clickRegisterButton();

        // Wait for the error message to appear in the document
        await waitFor(() => expect(screen.getByText("Passwords do not match.")).toBeInTheDocument());
    });

    test("shows validation message for invalid email", async () => {
        render(Register);
        await fillForm("testuser", "invalid-email", "password123", "password123");
        await clickRegisterButton();

        // Wait for the error message to appear in the document
        await waitFor(() => expect(screen.getByText("Please provide a valid email address.")).toBeInTheDocument());
    });

    test("does not allow submission if required fields are empty", async () => {
        render(Register);
        const registerButton = screen.getByRole("button", { name: "Register" });
        await fireEvent.click(registerButton);

        // Wait for the error message to appear in the document
        await waitFor(() => expect(screen.getByText("A field is missing. Please try again.")).toBeInTheDocument());
    });

    test("successfully registers a user", async () => {
        (global.fetch as vi.Mock).mockResolvedValueOnce({
            ok: true,
            json: async () => ({ message: "Registration successful!" }),
        });
        render(Register);
        await fillForm("testuser", "test@example.com", "password123", "password123");
        await clickRegisterButton();

        // Wait for success message
        await waitFor(() => expect(screen.getByText("Registration successful!")).toBeInTheDocument());
    });

    test("shows error when registration fails", async () => {
        (global.fetch as vi.Mock).mockResolvedValueOnce({
            ok: false,
            json: async () => ({ message: "Username or email was already taken. Please try again." }),
        });
        render(Register);
        await fillForm("testuser", "test@example.com", "password123", "password123");
        await clickRegisterButton();

        // Wait for error message
        await waitFor(() => expect(screen.getByText("Username or email was already taken. Please try again.")).toBeInTheDocument());
    });

    // Additional tests to handle form submission behavior
    describe("Additional Tests", () => {
        test("disables the submit button when loading", async () => {
            (global.fetch as vi.Mock).mockResolvedValueOnce({
                ok: true,
                json: async () => ({ message: "Registration successful!" }),
            });
            render(Register);
            await fillForm("testuser", "test@example.com", "password123", "password123");
            const registerButton = screen.getByRole("button", { name: "Register" });
            await fireEvent.click(registerButton);

            // Ensure the button is disabled while loading
            await waitFor(() => expect(registerButton).toBeDisabled());
        });

        test("shows a loading indicator when submitting", async () => {
            (global.fetch as vi.Mock).mockResolvedValueOnce({
                ok: true,
                json: async () => ({ message: "Registration successful!" }),
            });
            render(Register);
            await fillForm("testuser", "test@example.com", "password123", "password123");
            await fireEvent.click(screen.getByRole("button", { name: "Register" }));

            // Wait for the loading text to appear
            await waitFor(() => expect(screen.getByText("Loading...")).toBeInTheDocument());
        });

        test("shows error message when API call fails", async () => {
            (global.fetch as vi.Mock).mockResolvedValueOnce({
                ok: false,
                json: async () => ({ message: "Unexpected error occurred." }),
            });
            render(Register);
            await fillForm("testuser", "test@example.com", "password123", "password123");
            await fireEvent.click(screen.getByRole("button", { name: "Register" }));

            // Wait for the error message to appear
            await waitFor(() => expect(screen.getByText("Unexpected error occurred.")).toBeInTheDocument());
        });
    });
});
