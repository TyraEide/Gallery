import { render, fireEvent, screen, waitFor } from "@testing-library/svelte";
import userEvent from "@testing-library/user-event";
import Register from "../pages/Register.svelte";
import { beforeEach, expect, test, vi } from "vitest";
import '@testing-library/jest-dom';

// Mock the redirect function
vi.mock("../ts_modules/routing", () => ({
    redirect: vi.fn(),
}));

// Mock fetch globally
global.fetch = vi.fn();

describe("Register Component", () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

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

        await userEvent.type(screen.getByLabelText("Username"), "testuser");
        await userEvent.type(screen.getByLabelText("Email"), "test@example.com");
        await userEvent.type(screen.getByLabelText("Password"), "short");
        await userEvent.type(screen.getByLabelText("Confirm Password"), "short");

        await fireEvent.submit(screen.getByRole("button", { name: "Register" }));

        expect(screen.getByText("Password must be at least 8 characters.")).toBeInTheDocument();
    });

    test("shows validation message for mismatched passwords", async () => {
        render(Register);

        await userEvent.type(screen.getByLabelText("Username"), "testuser");
        await userEvent.type(screen.getByLabelText("Email"), "test@example.com");
        await userEvent.type(screen.getByLabelText("Password"), "password123");
        await userEvent.type(screen.getByLabelText("Confirm Password"), "password321");

        await fireEvent.submit(screen.getByRole("button", { name: "Register" }));

        expect(screen.getByText("Passwords do not match.")).toBeInTheDocument();
    });

    test("shows validation message for invalid email", async () => {
        render(Register);

        await userEvent.type(screen.getByLabelText("Username"), "testuser");
        await userEvent.type(screen.getByLabelText("Email"), "invalid-email");
        await userEvent.type(screen.getByLabelText("Password"), "password123");
        await userEvent.type(screen.getByLabelText("Confirm Password"), "password123");

        await fireEvent.submit(screen.getByRole("button", { name: "Register" }));

        expect(screen.getByText("Please provide a valid email address.")).toBeInTheDocument();
    });

    test("successfully registers a user", async () => {
        (global.fetch as vi.Mock).mockResolvedValueOnce({
            ok: true,
            json: async () => ({ message: "Registration successful!" }),
        });

        render(Register);

        await userEvent.type(screen.getByLabelText("Username"), "testuser");
        await userEvent.type(screen.getByLabelText("Email"), "test@example.com");
        await userEvent.type(screen.getByLabelText("Password"), "password123");
        await userEvent.type(screen.getByLabelText("Confirm Password"), "password123");

        await fireEvent.submit(screen.getByRole("button", { name: "Register" }));

        await waitFor(() => expect(screen.getByText("Registration successful!")).toBeInTheDocument());
    });

    test("shows error when registration fails", async () => {
        (global.fetch as vi.Mock).mockResolvedValueOnce({
            ok: false,
            json: async () => ({ message: "Username or email was already taken. Please try again." }),
        });

        render(Register);

        await userEvent.type(screen.getByLabelText("Username"), "testuser");
        await userEvent.type(screen.getByLabelText("Email"), "test@example.com");
        await userEvent.type(screen.getByLabelText("Password"), "password123");
        await userEvent.type(screen.getByLabelText("Confirm Password"), "password123");

        await fireEvent.submit(screen.getByRole("button", { name: "Register" }));

        await waitFor(() => expect(screen.getByText("Username or email was already taken. Please try again.")).toBeInTheDocument());
    });
});
