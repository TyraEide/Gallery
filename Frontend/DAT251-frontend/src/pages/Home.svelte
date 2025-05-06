<script lang="ts">
    import {build_page_url, redirect} from "../ts_modules/routing"
    import {onMount} from "svelte";
    import {user} from "../ts_modules/auth";

    onMount(async () => {
        //localStorage.clear();
        if (!$user) {
            redirect("login");
        }
    });
</script>

<main>
    <h2>Homepage Placeholder</h2>
    <a href={build_page_url("dashboard")}>Dashboard</a> |
    <a href={build_page_url("settings")}>Settings</a> |
    <a href={build_page_url("admin/settings")}>Admin settings</a>

</main>