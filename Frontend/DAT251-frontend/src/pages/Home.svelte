<script lang="ts">
    import {build_page_url} from "../ts_modules/routing"
    import {type User, get_logged_in_user} from "../ts_modules/api";
    import {onMount} from "svelte";

    let user: User = null;

    onMount(async () => {
        localStorage.clear();
        user = get_logged_in_user();
    });
</script>

<main>
    <h2>Homepage Placeholder</h2>

    {#if user}
        <a href={build_page_url("dashboard")}>Dashboard</a> |
        <a href={build_page_url("settings")}>Settings</a> |
        <a href={build_page_url("admin/settings")}>Admin settings</a>
    {:else}
        <a href={build_page_url("login")}>Login</a> |
        <a href={build_page_url("register")}>Register</a>
    {/if}

</main>