import { render, fireEvent, screen, waitFor } from "@testing-library/svelte";
import userEvent from "@testing-library/user-event";
import Register from "../pages/Register.svelte";
import { beforeEach, expect, test, vi, describe } from "vitest";
import "@testing-library/jest-dom";

vi.mock("../ts_modules/routing", () => ({
    redirect: vi.fn(),
}));

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

    test("renders the registration form correctly", () => {
        render(Register);
        expect(screen.getByText("Register User")).toBeInTheDocument();
        expect(screen.getByLabelText("Username")).toBeInTheDocument();
        expect(screen.getByLabelText("Email")).toBeInTheDocument();
        expect(screen.getByLabelText("Password")).toBeInTheDocument();
        expect(screen.getByLabelText("Confirm Password")).toBeInTheDocument();
        expect(screen.getByRole("button", { name: "Register" })).toBeInTheDocument();
    });

    test("shows error when required fields are empty and does not submit", async () => {
        render(Register);
        await clickRegisterButton();
        await waitFor(() => expect(screen.getByText("A field is missing. Please try again.")).toBeInTheDocument());
        expect(global.fetch).not.toHaveBeenCalled();
    });

    test("shows error for invalid email format and does not submit", async () => {
        render(Register);
        await fillForm("testuser", "invalid-email", "password123", "password123");
        await clickRegisterButton();
        await waitFor(() => expect(screen.getByText("Please provide a valid email address.")).toBeInTheDocument());
        expect(global.fetch).not.toHaveBeenCalled();
    });

    test("shows error for short password and does not submit", async () => {
        render(Register);
        await fillForm("testuser", "test@example.com", "short", "short");
        await clickRegisterButton();
        await waitFor(() => expect(screen.getByText("Password must be at least 8 characters.")).toBeInTheDocument());
        expect(global.fetch).not.toHaveBeenCalled();
    });

    test("shows error for mismatched passwords and does not submit", async () => {
        render(Register);
        await fillForm("testuser", "test@example.com", "password123", "password321");
        await clickRegisterButton();
        await waitFor(() => expect(screen.getByText("Passwords do not match.")).toBeInTheDocument());
        expect(global.fetch).not.toHaveBeenCalled();
    });

    test("successfully registers a user and shows success message", async () => {
        (global.fetch as vi.Mock).mockResolvedValueOnce({
            ok: true,
            json: async () => ({ message: "Registration successful!" })
        });
        render(Register);
        await fillForm("testuser", "test@example.com", "password123", "password123");
        await clickRegisterButton();
        await waitFor(() => expect(screen.getByText("Registration successful!")).toBeInTheDocument());
    });

    test("shows error when username or email is already taken", async () => {
        (global.fetch as vi.Mock).mockResolvedValueOnce({
            ok: false,
            json: async () => ({ message: "Username or email was already taken. Please try again." })
        });
        render(Register);
        await fillForm("testuser", "test@example.com", "password123", "password123");
        await clickRegisterButton();
        await waitFor(() => expect(screen.getByText("Username or email was already taken. Please try again.")).toBeInTheDocument());
    });

    test("disables register button during form submission", async () => {
        (global.fetch as vi.Mock).mockResolvedValueOnce({
            ok: true,
            json: async () => ({ message: "Registration successful!" })
        });
        render(Register);
        await fillForm("testuser", "test@example.com", "password123", "password123");
        const registerButton = screen.getByRole("button", { name: "Register" });
        await fireEvent.click(registerButton);
        await waitFor(() => expect(registerButton).toBeDisabled());
    });

    test("shows loading indicator while submitting", async () => {
        (global.fetch as vi.Mock).mockResolvedValueOnce({
            ok: true,
            json: async () => ({ message: "Registration successful!" })
        });
        render(Register);
        await fillForm("testuser", "test@example.com", "password123", "password123");
        await clickRegisterButton();
        await waitFor(() => expect(screen.getByText("Loading...")).toBeInTheDocument());
    });

    test("ensures CSRF token is included in the request headers", async () => {
        (global.fetch as vi.Mock).mockImplementation((url, options) => {
            const headers = new Headers(options?.headers);
            expect(headers.get("X-CSRF-Token")).not.toBeNull();
            return Promise.resolve({
                ok: true,
                json: async () => ({ message: "Registration successful!" })
            });
        });
        render(Register);
        await fillForm("testuser", "test@example.com", "password123", "password123");
        await clickRegisterButton();
    });
});
