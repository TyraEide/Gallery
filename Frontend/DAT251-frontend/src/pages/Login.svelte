
<script lang="ts">

    import {redirect } from "../ts_modules/routing"

    // General notice ⚠️
    // Session ID is not set up correctly
    // Should be set with set_cookie header / HTTPS
    // Causes error when host domain == client domain

    // General notice ⚠️
    // Application should be modified to communicate with back end instead of python flask test server

    let username:       string = ""
    let password:       string = ""
    let cookie_val:     string = ""
    let response_msg:   string = ""
  
    function setCookie(cookie_name:string,val:string){
          // ⚠️ WARNING : This cookie will not be sent with other requests due to misaligned domain
          // Session ID should be set with the set_cookie header. Use HTTPS 
          document.cookie = cookie_name + " = " + val
          
    }
    
    // Default login request to specified URL
    async function login_request(url:string){
      // request
      const response_json = await fetch(url, {method: "POST", credentials: "include", headers: {
          'content-type': 'application/json'
          }, body: JSON.stringify({
                  username: username,
                  password: password
              })
      }).then((r) => r.json())

      // output value
      response_msg = response_json.message

      // set sessionID cookie from JSON
      if(response_json.sessionID != undefined){
        setCookie("SessionID",response_json.sessionID)
        redirect()
      }
      else{
        console.log("Missing Cookie")
      }
      
    }

    // Debug function for "localhost:5000"
    async function check_message_cookie_req(){
      const response_json = await fetch("http://127.0.0.1:5000/cookie", {credentials: "include"}).then((r) => r.json())
      response_msg = response_json.message
    }
  
  </script>
  
  <main>
    <div>
      <h2>Login Section</h2>
      <div>
        Username: <input type="text" bind:value={username}><br>
        Password: <input type="text" bind:value={password}><br>
        <button onclick={() => {login_request("http://127.0.0.1:5000/login")}}>Login</button>
      </div>

      <div>
        <p>Output: {response_msg}<p>

        <p>Do you not have an account?<p>
        <button onclick={() => {redirect("register")}}>Register user</button>
      </div>
    </div>

  
    <div>
      <h2>Debug Section</h2>
      <button onclick={() => {check_message_cookie_req()}}>Check if cookie?</button>
    </div>
  
    <a href="https://vite.dev">Vite docs</a><br>
    <a href="https://svelte.dev">Svelte docs</a>
  </main>
   