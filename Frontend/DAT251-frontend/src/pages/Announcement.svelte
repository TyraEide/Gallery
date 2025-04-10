<script lang="ts">
  import { onMount } from "svelte";
  import config from "../config";

  type DiscussionTopic = {
    title: string;
    message: string;
    author: { name: string };
    posted_at: string;
    is_announcement: boolean;
  };

  let DiscussionTopics: DiscussionTopic[] = [];
  let selectedTopic: DiscussionTopic | null = null;

  async function fetchAnnouncements() {
    try {
      const response = await fetch(
        `${config.API_BASE_URL}/api/courses/announcements`,
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
    } catch (error) {
      console.error("Error fetching announcements:", error);
    }
  }

  function selectTopic(topic: DiscussionTopic) {
    selectedTopic = topic;
  }

  function goBack() {
    selectedTopic = null;
  }

  onMount(fetchAnnouncements);
</script>

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
