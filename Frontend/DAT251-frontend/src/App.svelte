<script lang="ts">
  import Router from "svelte-spa-router";
  import Login from "./pages/Login.svelte";
  import PageMissing from "./pages/Err404.svelte";
  import Home from "./pages/Home.svelte";
  import Dashboard from "./pages/Dashboard.svelte";
  import Register from "./pages/Register.svelte";
  import RegistrationSuccessful from "./pages/RegistrationSuccessful.svelte";
  import Announcement from "./pages/Announcement.svelte";
  import Course from "./pages/Course.svelte";
  import Settings from "./pages/Settings.svelte";
  import AdminSettings from "./pages/AdminSettings.svelte";
  import NewInstitution from "./pages/NewInstitution.svelte";
  import NewCanvasToken from "./pages/NewCanvasToken.svelte";

  import CustomSidebar from "./pages/Sidebar.svelte";

  // Routing paths
  let routes = {
    "/": Home,
    "/login": Login,
    "/register": Register,
    "/registrationSuccessful": RegistrationSuccessful,
    "/announcement": Announcement,
    "/dashboard": Dashboard,
    "/course/:id": Course, // Dynamic route for courses
    "/settings": Settings,
    "/settings/add_token": NewCanvasToken,
    "/admin/settings": AdminSettings,
    "/admin/settings/add_institution": NewInstitution,
    "*": PageMissing
  };
</script>

<main>
  <CustomSidebar/>
  <Router {routes} />
</main> 