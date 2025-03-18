<script lang="ts">
    import { onMount } from "svelte";
    import {redirect } from "../ts_modules/routing" 
    
    let username: string = "";
    let password: string = "";
    let confirmPassword: string = "";
    let message: string = "";
    let csrfToken: string = "";
  
    // Fetch CSRF token on mount (ensure your backend sends this)
    onMount(async () => {
      const tokenMeta = document.querySelector('meta[name="csrf-token"]');
      csrfToken = tokenMeta ? tokenMeta.getAttribute("content") || "" : "";
    });
  
    // Handle registration
    async function register(event: Event) {
      event.preventDefault(); // Prevent page reload
  
      if (password.length < 8) {
        message = "Password must be at least 8 characters.";
        return;
      }
  
      if (password !== confirmPassword) {
        message = "Passwords do not match.";
        return;
      }
  
      try {
        // Send the registration request to your backend
        const response = await fetch("http://localhost:8080/api/auth/register", {
          method: "POST",
          credentials: "include", // Send cookies with the request
          headers: {
            "Content-Type": "application/json",
            "X-CSRF-Token": csrfToken, // Include CSRF token in header if needed
          },
          body: JSON.stringify({ username, password }),
        });
  
        const data = await response.json();
        message = data.message || "Registration successful!";
  
        if (response.ok) {
          // Redirect to login page after successful registration
          setTimeout(() => redirect("/login"), 1500);
        }
      } catch (error) {
        message = "Error registering. Please try again.";
        console.error(error);
      }
    }
  </script>
  
  <main>
    <h2>Register</h2>
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
  
      <button type="submit">Register</button>
    </form>
    <p>{message}</p>
  </main>
  
  <style>
    form {
      display: flex;
      flex-direction: column;
      gap: 10px;
      max-width: 300px;
      margin: auto;
    }
    button {
      margin-top: 10px;
    }
  </style>
  