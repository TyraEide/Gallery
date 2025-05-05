<script lang="ts">
    import {build_page_url} from "../ts_modules/routing"
</script>
<h2>Homepage Placeholder</h2>

<a href={build_page_url("login")}>Login</a> |
<a href={build_page_url("register")}>Register</a> |
<a href={build_page_url("dashboard")}>Dashboard</a> |
<a href={build_page_url("settings")}>Settings</a> |
<a href={build_page_url("admin/settings")}>Admin settings</a>