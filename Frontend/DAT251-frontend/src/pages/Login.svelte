<script lang="ts">

  import {api_url,set_jwt_token,jwt_token_header} from "../ts_modules/api"
  import {redirect } from "../ts_modules/routing"

  console.log("page loaded")

  // TODO
  // CSRF implementation ?

  let email:          string = ""
  let password:       string = ""

  let message:        string = "-"
  let cached_email:   string = ""

  let loading:        boolean = false

  // Login request
  async function login_request(){
    if(cached_email == ""){

      cached_email = email // to avoid concurrency errors // working as lock too
      message = "Waiting for response"

      let request_failed = false
      
      // request
      const response_json = await fetch(api_url("/login"), 
        {method: "POST", 
        signal : AbortSignal.timeout(5000),
        headers: {
          'content-type': 'application/json'
          }, body: JSON.stringify({
                  email: email,
                  password: password
              })
      }).then((r) => r.json()).catch(() => request_failed = true)

      if(request_failed){
        cached_email = ""
        message = "Failed to reach server"
        return
      }

      // output value
      message = response_json.message

      // set sessionID cookie from JSON
      if(response_json.token != undefined && response_json.user != undefined){
        if(response_json.user.email == cached_email){
          set_jwt_token(response_json.token)
          setTimeout(() => redirect("dashboard"), 600)
        }
      }
      else{
        console.log("malformed response")
      }

      cached_email = ""
    }
    else{
      console.log("Already waiting for response")
    }
    
  }

  async function jwt_sample_req(){
    const headers = jwt_token_header();
    const response_json = await fetch(api_url("/jwt"),{headers}).then((r) => r.json())
    message = response_json.message
  }

</script>

<main>
  <div>

    <h2>Login</h2>
    <form onsubmit={() => login_request()}>

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
            autocomplete="current-password"
        />

        <button type="submit" disabled={loading}> 
            {#if loading}
                <span class="spinner"></span> Loading...
            {:else}
                Login
            {/if}
        </button>

        
    </form>
    
    <p>{message}</p>

    <hr>

    Sign up instead?
    <p><button type="button" onclick={() => {redirect("register")}}>Register account</button></p>
  </div>

  <hr>


  <div>
    <h2>Debug Section</h2>
    <button onclick={() => {jwt_sample_req()}}>Check valid JWT?</button>
  </div>

  <a href="https://vite.dev">Vite docs</a><br>
  <a href="https://svelte.dev">Svelte docs</a>
</main>
 
<style>
</style>