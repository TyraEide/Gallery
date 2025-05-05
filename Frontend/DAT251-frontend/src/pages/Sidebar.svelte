<script lang="ts">
    import {build_page_url} from "../ts_modules/routing";
</script>

<style>
    .sidebar a {
        display: block;
        padding: 16px;
        padding-left: 12px;
        text-decoration: none;
        color: white;
        border-bottom-style: dotted;
        border-bottom-width: 3px;
        border-bottom-color: rgb(184, 230, 184);
    }

    .sidebar h2{
        padding-left: 12px;
    }

    .sidebar a:hover{
        background-color: rgba(255, 255, 255, 0.122);
    }

    .sidebar img{
        width: 100px;
        padding: 20px;
        padding-bottom: 0px;
    }

    .sidebar {
        position: fixed;
        width: 150px;
        height: 100%;
        color: white;
        top: 0;
        left: 0;
        border-right-style: solid;
        border-right-width: 3px;
        border-right-color: white;
        text-align: start;
        background-color: #1f6c12;
    }
 
</style>


<div class="sidebar">
    <img src="../public/iconGallery.png"/>
    <a href={build_page_url("")}>Home</a>
    <a href={build_page_url("Dashboard")} >Dashboard</a>
    <a href={build_page_url("Login")}>Login</a>
    <a href={build_page_url("Settings")}>⚙ Settings</a>
    <a href={build_page_url("")} onclick={localStorage.clear()}>↩ Logout</a>
  </div>