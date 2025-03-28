<script lang="ts">
    import { onMount } from "svelte";
    import { redirect } from "../ts_modules/routing"; 

    let username: string = "";
    let email: string = "";
    let password: string = "";
    let confirmPassword: string = "";
    let message: string = "";
    let csrfToken: string = "";
    let loading: boolean = false; 

    async function register(event: Event) {
        event.preventDefault();

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


        

        try {

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
            } else {
                message = data.message || "Registration successful!";

                setTimeout(() => {
                    redirect("registrationSuccessful");
                }, 1500);

                
            }
        } catch (error) {
            message = "A field is missing. Please try again.";
            console.error(error);
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
            required
            minlength="3"
            maxlength="30"
            autocomplete="off"
        />

        <label for="email">Email</label>
        <input
            id="email"
            type="text"
            bind:value={email}
            required
            autocomplete="email"
        />

        <label for="password">Password</label>
        <input
            id="password"
            type="password"
            bind:value={password}
            required
            minlength="8"
            autocomplete="new-password"
        />

        <label for="confirmPassword">Confirm Password</label>
        <input
            id="confirmPassword"
            type="password"
            bind:value={confirmPassword}
            required
            minlength="8"
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
