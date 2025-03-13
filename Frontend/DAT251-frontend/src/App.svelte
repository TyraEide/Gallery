<script lang="ts">
  import Router from "svelte-spa-router";
  import Login from "./pages/Login.svelte";
  import PageMissing from "./pages/Err404.svelte";
  import Home from "./pages/Home.svelte";

  // Routing paths
  let routes = {
    "/": Home,
    "/login": Login,
    "*": PageMissing
  }
</script>

<main>
  <Router {routes}/>
</main>