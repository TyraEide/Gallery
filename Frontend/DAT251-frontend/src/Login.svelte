
<script lang="ts">

    let username:       string = ""
    let password:       string = ""
    let cookie_val:     string = ""
    let response_msg:   string = ""
  
    function responseMessageHandler(r:Promise<Response>,output_variable:string){
          r.then((n) => n.json()).then((j) => output_variable = j.message).catch(() => console.log("Missing message"))
    }
  
    function setCookie(cookie_name:string,val:string){
          document.cookie = cookie_name + " = " + val
    }
    
    function login_request(url:string){
  
      const req = fetch(url, {method: "POST", credentials: "include", headers: {
          'content-type': 'application/json'
          }, body: JSON.stringify({
                  username: username,
                  password: password
              })
      })
  
      responseMessageHandler(req,response_msg)
  
      req .then((r) => r.json())
          .then((j) => setCookie("SessionID",j.sessionID))
          .catch(() => console.log("no session ID"))
    }
  
  </script>
  
  <main>
    funny working<br>
  
    <div>
      Output: {response_msg}
    </div>
  
    <div>
      <input type="text" bind:value={cookie_val}>
      <input type="submit" onclick={() => {setCookie("SessionID",cookie_val)}}>
    </div>
  
    <div>
      <input type="text" bind:value={username}>
      <input type="text" bind:value={password}>
      <input type="submit" onclick={() => {login_request("http://127.0.0.1:5000/login")}}>
    </div>
  
    <div>              
      <button onclick={() => {}}>Check if cookie?</button>
    </div>
  
    <a href="https://vite.dev">Vite docs</a><br>
    <a href="https://svelte.dev">Svelte docs</a>
  </main>
   
  <style>
  </style>