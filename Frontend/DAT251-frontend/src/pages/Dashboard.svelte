<script lang="ts">
    import { onMount } from "svelte";
    import { api_url } from "../ts_modules/api";
    import {redirect} from "../ts_modules/routing";
    import Announcement from "./Announcement.svelte";

    interface User {
        name:String
    }

    const monthNames = ["January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    ];

    const date = new Date();
    let courses : JSON;
    let calendar_events : JSON;
    let user : User = {name:"User"}

    async function getCourses(){
        const r_json = fetch(api_url("/courses"))
                        .then((r) => r.json())
                        .catch((e) => console.error(e))

        return r_json
    }

    async function getEvents(){
        const r_json = fetch(api_url("/calendar"))
                        .then((r) => r.json())
                        .catch((e) => console.error(e))

        return r_json
    }

    async function getUser(){
        const r_json = fetch(api_url("/users"))
                        .then((r) => r.json())
                        .catch((e) => console.error(e))

        return r_json
    }

    onMount( async () => {
        //getCourses().then((r)=> courses = r)
        //getEvents().then((r) => calendar_events = r)
        
    })


</script>

<main>
    <h2>Welcome {user.name}</h2>
    <h2>{date.toLocaleString("default", {weekday:'long',day:'numeric',month:'long',year:'numeric'})}</h2>
    <Announcement />
</main>