<script lang="ts">
  import { onMount } from "svelte";
  import { redirect } from "../ts_modules/routing";
  import {get_logged_in_user, type User} from "../ts_modules/api";
  import config from "../config"
  import Announcement from "./Announcement.svelte";
  import type {UUID} from "node:crypto";

  interface Course { id: number; name: string; code: string }

  let courses: Course[] = [];
  let user: User = null;
  let displayName: String = "";

  async function getCourses(): Promise<Course[]> {
    const res = await fetch(`${config.API_BASE_URL}/api/courses`);
    return res.json();
  }

  function goToLink(link: string) {
    window.location.href = link;
  }

  onMount(async () => {
    user = get_logged_in_user();
    try {
      courses = await getCourses();
    } catch (e) {
      console.error("Failed to load courses", e);
    }

    if(user == null){
      redirect("login")
    }

    displayName = user.username;
  });
</script>

<main>
  <div class="header">
    <h2>Welcome {displayName}</h2>
    <h3>{new Date().toLocaleString("default",{weekday:"long",day:"numeric",month:"long",year:"numeric"})}</h3>
    <button on:click={() => redirect("announcements")}>Announcements</button>
    <button on:click={() => goToLink('https://hvl.instructure.com')}>HVL Canvas</button>
    <button on:click={() => goToLink('https://mitt.uib.no/login/canvas')}>UIB Canvas</button>
    <button on:click={() => redirect("settings")}>Settings</button>
  </div>
  <section class="announcement-container">
    <Announcement />
  </section>
  <section class="courses-grid">
    {#each courses as c}
      <div class="course-card" on:click={() => redirect(`course/${c.id}`)}>
        <h3>{c.name}</h3>
        <p>{c.code}</p>
      </div>
    {/each}
  </section>
</main>