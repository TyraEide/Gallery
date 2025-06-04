<script lang="ts">
  import { onMount } from "svelte";
  import config from "../config";
  import {user, type User} from "../ts_modules/auth";

  type DiscussionTopic = {
    title: string;
    message: string;
    author: { name: string };
    posted_at: string;
    is_announcement: boolean;
  };

  let DiscussionTopics: DiscussionTopic[] = [];
  let selectedTopic: DiscussionTopic | null = null;
  let errorMessage: string | null = null;
  let isLoading: boolean = true

  async function fetchAnnouncements() {
    try {
      let id = $user.id;
      const response = await fetch(
        `${config.API_BASE_URL}/api/courses/announcements/users/${id}`,
      );
      if (!response.ok) throw new Error("Failed to fetch announcements");
      const data = await response.json();
      DiscussionTopics = data
        .filter((item: any) => item.is_announcement)
        .map((item: any) => ({
          title: item.title,
          message: item.message,
          author: { name: item.author.name },
          posted_at: item.posted_at,
          is_announcement: item.is_announcement,
        }));
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
    {#each DiscussionTopics as DT}
      <li class="announcement">
        <button
          type="button"
          on:click={() => selectTopic(DT)}
          tabindex="0"
          class="announcement-button"
        >
          <h3>{DT.title}</h3>
          <p>{DT.message}</p>
          <small
            >Posted by {DT.author.name} on {new Date(
              DT.posted_at,
            ).toLocaleString()}</small
          >
        </button>
      </li>
    {/each}
  </ul>
{/if}
