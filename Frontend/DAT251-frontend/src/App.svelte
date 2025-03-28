<script lang="ts">
  import Router from "svelte-spa-router";
  import Login from "./pages/Login.svelte";
  import PageMissing from "./pages/Err404.svelte";
  import Home from "./pages/Home.svelte";
  import Register from "./pages/Register.svelte";
  import RegistrationSuccessful from "./pages/RegistrationSuccessful.svelte";

  // Routing paths
  let routes = {
    "/": Home,
    "/login": Login,
    "/register": Register,
    "/registrationSuccessful": RegistrationSuccessful,
    "*": PageMissing
  }
</script>

<main>
  <Router {routes}/>
</main>