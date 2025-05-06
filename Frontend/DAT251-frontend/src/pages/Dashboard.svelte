<script lang="ts">
  import { onMount } from "svelte";
  import { redirect } from "../ts_modules/routing";
  import config from "../config"
  import Announcement from "./Announcement.svelte";
  import {user} from "../ts_modules/auth";

  interface Course { id: number; name: string; code: string }
  interface CourseMap {[key: string]: Course[] }

  let courses: CourseMap = {};
  let displayName: String = "";

  async function getCourses(): Promise<CourseMap> {
    let id = $user.id;
    const res = await fetch(`${config.API_BASE_URL}/api/courses/users/${id}`);
    return res.json();
  }

  function goToLink(link: string) {
    window.location.href = link;
  }

  onMount(async () => {
    try {
      courses = await getCourses();
    } catch (e) {
      console.error("Failed to load courses", e);
    }

    if(!$user){
      redirect("login")
    }

    displayName = $user.username;
  });
</script>

<main>
  <div class="header">
    <h2>Welcome {displayName}</h2>
    <h3>{new Date().toLocaleString("default",{weekday:"long",day:"numeric",month:"long",year:"numeric"})}</h3>
<<<<<<< HEAD
    <button on:click={() => goToLink('https://hvl.instructure.com')}>HVL Canvas</button>
    <button on:click={() => goToLink('https://mitt.uib.no/login/canvas')}>UIB Canvas</button>
=======
>>>>>>> 9ebc8838c6526c426524dc645f7f0462e00f708a
    <button on:click={() => redirect("settings")}>Settings</button>
  </div>
  <section class="announcement-container">
    <Announcement />
  </section>
  <section class="courses-container">
    {#each Object.entries(courses) as [institution, courseList]}
      <h3>{institution}</h3>
      <div class="courses-grid">
        {#each courseList as c}
          <div class="course-card" on:click={() => redirect(`course/${c.id}`)}>
            <h3>{c.name}</h3>
            <p>{c.code}</p>
          </div>
          {/each}
      </div>
    {/each}
  </section>
</main>