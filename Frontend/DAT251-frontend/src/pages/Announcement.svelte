<script lang="ts">
  import {onMount} from "svelte";
  import config from "../config";
  import {user} from "../ts_modules/auth";

  type DiscussionTopic = {
    title: string;
    message: string;
    author: { name: string };
    posted_at: string;
    is_announcement: boolean;
  };

  interface Course { id: number; name: string; code: string }
  interface AnnouncementMap {[key: string]: {
      course: Course;
      announcements: DiscussionTopic[];
    }
  }

  let announcements: AnnouncementMap = {};
  let selectedTopic: DiscussionTopic | null = null;
  let errorMessage: string | null = null;
  let isLoading: boolean = true

  async function fetchAnnouncements() {
    try {
      let id = $user.id;
      const response = await fetch(
        `${config.API_BASE_URL}/api/courses/announcements/users/${id}`
      );
      if (!response.ok) throw new Error("Failed to fetch announcements");
      announcements = await response.json();
      errorMessage = null;
    } catch (error) {
      console.error("Error fetching announcements:", error);
      errorMessage = "Could not fetch announcements: " + error
    } finally {
      isLoading = false;
    }
  }

  function selectTopic(topic: DiscussionTopic) {
    selectedTopic = topic;
  }

  function goBack() {
    selectedTopic = null;
  }

  onMount(async () => {
    await fetchAnnouncements();
  })
</script>

{#if isLoading}
  <div class=loader-container>
    <div class=loader></div>
    <div class=loader-text>Loading...</div>
  </div>
{/if}

{#if errorMessage}
  <p>{errorMessage}</p>
{/if}

{#if selectedTopic}
  <div class="selected-topic">
    <h2>{selectedTopic.title}</h2>
    <p>{selectedTopic.message}</p>
    <small
      >Posted by {selectedTopic.author.name} on {new Date(
        selectedTopic.posted_at,
      ).toLocaleString()}</small
    >
  </div>
  <button on:click={goBack}>Back</button>
{:else}
  <ul>
    {#each Object.entries(announcements) as [institution, courseMap]}
      <h3>{institution}</h3>
      {#each courseMap.announcements as a}
      <li class="announcement">
        <button
          type="button"
          on:click={() => selectTopic(a)}
          tabindex="0"
          class="announcement-button"
        >
          <h3>{a.title}</h3>
          <p>{a.message}</p>
          <small
            >Posted by {a.author.name} on {new Date(
              a.posted_at,
            ).toLocaleString()}</small
          >
        </button>
      </li>
        {/each}
    {/each}
  </ul>
{/if}
