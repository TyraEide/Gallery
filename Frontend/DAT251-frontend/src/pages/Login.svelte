<script lang="ts">

  import {api_url,set_jwt_token,jwt_token_header} from "../ts_modules/api"
  import {redirect } from "../ts_modules/routing"

  console.log("page loaded")

  // TODO
  // CSRF implementation ?

  let email:          string = ""
  let password:       string = ""

  let message:        string = "..."
  let cached_email:   string = ""


  // Login request
  async function login_request(event:Event){

    event.preventDefault()

    cached_email = email // to avoid concurrency errors // working as lock too

    try{

      // request
      const response = await fetch(api_url("/auth/login"), 
        {method: "POST", 
        signal : AbortSignal.timeout(10000),
        headers: {
          'content-type': 'application/json'
          }, body: JSON.stringify({
                  email: email,
                  password: password
              })
      })

      if(!response.ok){
        switch(response.status){
          case 401: 
            message = "Incorrect email or password"
            break

          default:
            message = "unknown error (status error)"
            console.log("response status code:" + response.status)
            break

        }
      }
      else{

        const response_json = await response.json()

        //check for contents
        if(response_json.token != undefined){
          
            set_jwt_token(response_json.token)
            message = "Login Successful"
            setTimeout(() => redirect("dashboard"), 600)
        }
        else{
          message = response_json.token
          console.log("malformed response error")
        }
      }
    } 
    catch(err){
      message = "Issue reaching server | " + (err)
    } 

    cached_email = ""
  }

  // For debugging purpouses
  async function jwt_sample_req(){
    const headers = jwt_token_header();
    const response_json = await fetch(api_url("/jwt"),{headers}).then((r) => r.json())
    message = response_json.message
  }

</script>

<main>
  <div>

    <h2>Login</h2>
    <form onsubmit={login_request}>

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

        <button type="submit" disabled={cached_email != ""}>
            {#if cached_email != ""}
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
</main>
 
<style>
</style>