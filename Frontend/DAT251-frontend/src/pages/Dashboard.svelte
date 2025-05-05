<script lang="ts">
  import { onMount } from "svelte";
  import { redirect } from "../ts_modules/routing";
  import config from "../config"
  import Announcement from "./Announcement.svelte";
  import type {UUID} from "node:crypto";

  interface Course { id: number; name: string; code: string }
  interface User {username: string; email: string;}

  let courses: Course[] = [];
  let user: User = null;

  function getUser(): User {
    const user = localStorage.getItem("user");
    return user ? JSON.parse(user) : null
  }

  async function getCourses(): Promise<Course[]> {
    const res = await fetch(`${config.API_BASE_URL}/api/courses`);
    return res.json();
  }

  onMount(async () => {
    user = getUser();
    try {
      courses = await getCourses();
    } catch (e) {
      console.error("Failed to load courses", e);
    }
  });
</script>

<main>
  <div class="header">
    <h3>{new Date().toLocaleString("default",{weekday:"long",day:"numeric",month:"long",year:"numeric"})}</h3>
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