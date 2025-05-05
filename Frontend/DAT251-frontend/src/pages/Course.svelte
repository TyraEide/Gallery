<script lang="ts">
  import { onMount } from "svelte";
  import config from "../config";

  export let params: { id: string };
  let course: { id: number; name: string; code: string; description?: string } | null = null;

  async function getCourse(id: string) {
    const res = await fetch(`${config.API_BASE_URL}/api/courses/${id}`);
    if (!res.ok) throw new Error("Failed to fetch course");
    return res.json();
  }

  onMount(async () => {
    try {
      course = await getCourse(params.id);
    } catch (e) {
      console.error("Failed to load course", e);
    }
  });
</script>

<main>
  {#if course}
    <h1>{course.name}</h1>
    <p>Code: {course.code}</p>
    <p>{course.description || "No description available."}</p>
  {:else}
    <p>Loading course details...</p>
  {/if}
</main>