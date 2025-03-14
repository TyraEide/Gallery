<script lang="ts">
    import { redirect } from "../ts_modules/routing";
  
    let registerUsername: string = "";
    let registerPassword: string = "";
    let response_msg: string = "";
    
    
    // Registration request
    async function register_request(url: string) {
      if (registerPassword.length < 8) {
        response_msg = "Password must be at least 8 characters long.";
        return;
      }
  
      const response = await fetch(url, {
        method: "POST",
        credentials: "same-origin", // Ensures CSRF protection
        headers: { 
          "Content-Type": "application/json", 
          "X-CSRF-Token": getCsrfToken() // Fetch CSRF token (backend must provide)
        },
        body: JSON.stringify({ 
          username: registerUsername, 
          password: registerPassword 
        }),
      });
  
      const response_json = await response.json();
      response_msg = response_json.message;
  
      if (response.ok) {
        redirect();
      } else {
        console.log("Registration failed:", response_json);
      }
    }
  
    function getCsrfToken(): string {
      // Fetch CSRF token from meta tag (backend should insert this)
      const tokenMeta = document.querySelector('meta[name="csrf-token"]');
      return tokenMeta ? tokenMeta.getAttribute("content") || "" : "";
    }
    
  </script>
  
  <main>
    <div>
      <h2>Register Section</h2>
      <div>
        Username: <input type="text" bind:value={registerUsername} required minlength="3" maxlength="30" autocomplete="off"><br>
        Password: <input type="password" bind:value={registerPassword} required minlength="8" autocomplete="new-password"><br>
        <input type="submit" value="Register">
      </div>
      <div><strong>Output:</strong> {response_msg}</div>
    </div>
  </main>
  

  