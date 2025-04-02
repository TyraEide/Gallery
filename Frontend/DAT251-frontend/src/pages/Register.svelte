
<!--/*
This script is created with a lot of help from ChatGPT multiple times. Here is two of the revisions:
https://chatgpt.com/share/67ed0b01-c148-8007-960b-87bc9015966f
https://chatgpt.com/share/67ed0a56-54c0-8007-a6fa-672a3f8fe841
*/-->
<script lang="ts">    import { redirect } from "../ts_modules/routing"; 

    let username: string = "";
    let email: string = "";
    let password: string = "";
    let confirmPassword: string = "";
    let message: string = "";
    let csrfToken: string = "";
    let loading: boolean = false; 

    async function register(event: Event) {
        event.preventDefault();
        
        if (!username || !email || !password || !confirmPassword) {
            message = "A field is missing. Please try again.";
            return;
        }

        if (password.length < 8) {
            message = "Password must be at least 8 characters.";
            return;
        }

        if (password !== confirmPassword) {
            message = "Passwords do not match.";
            return;
        }

        if (!email.includes('.') || !email.includes('@')) {
            message = "Please provide a valid email address.";
            return;
        }

        loading = true;

        const response = await fetch("http://localhost:8080/api/users", {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
                "X-CSRF-Token": csrfToken,
            },
            body: JSON.stringify({ username, email, password }),
        });

        const data = await response.json();

        if (!response.ok) {
            message = data.message || "Username or email was already taken. Please try again.";
            loading = false;
        } else {
            message = data.message || "Registration successful!";

            setTimeout(() => {
                redirect("registrationSuccessful");
            }, 1500);

        }
        
    }
</script>

<main>
    <h2>Register User</h2>
    <form on:submit={register}>
        <label for="username">Username</label>
        <input
            id="username"
            type="text"
            bind:value={username}
            maxlength="30"
            autocomplete="off"
        />

        <label for="email">Email</label>
        <input
            id="email"
            type="text"
            bind:value={email}
            autocomplete="email"
        />

        <label for="password">Password</label>
        <input
            id="password"
            type="password"
            bind:value={password}
            autocomplete="new-password"
        />

        <label for="confirmPassword">Confirm Password</label>
        <input
            id="confirmPassword"
            type="password"
            bind:value={confirmPassword}
            autocomplete="new-password"
        />

        <button type="submit" disabled={loading}> 
            {#if loading}
                <span class="spinner"></span> Loading...
            {:else}
                Register
            {/if}
        </button>
    </form>
    <p>{message}</p>
</main>
