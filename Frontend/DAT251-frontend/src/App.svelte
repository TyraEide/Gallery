<script lang="ts">
  import Router from "svelte-spa-router";
  import Login from "./Login.svelte";
  import PageMissing from "./404.svelte";
  import Home from "./Home.svelte";

  let routes = {
    "/": Home,
    "/login": Login,
    "*": PageMissing
  }
</script>

<main>
  <Router {routes}/>
</main>