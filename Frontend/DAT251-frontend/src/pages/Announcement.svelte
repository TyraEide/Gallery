<script lang="ts">
  import { onMount } from "svelte";

  type DiscussionTopic = {
    title: string;
    message: string;
    author: { name: string };
    posted_at: string;
    is_announcement: boolean;
  };

  let DiscussionTopics: DiscussionTopic[] = [];

  async function fetchAnnouncements() {
    try {
      const response = await fetch("http://backend-API-url/announcements"); // Need to add the actual API where the announcements are located
      if (!response.ok) throw new Error("Failed to fetch announcements");

      const data = await response.json();
      DiscussionTopics = data
        .filter((item: any) => item.is_announcement)
        .map((item: any) => ({
          title: item.title,
          message: item.message,
          author: { name: item.author.name },
          posted_at: item.posted_at,
          is_announcement: item.is_announcement
        }));
    } catch (error) {
      console.error("Error fetching announcements:", error);
    }
  }

  onMount(fetchAnnouncements);
</script>

<style>
  ul {
    list-style: none;
    padding: 0;
  }
  .announcement {
    border: 1px solid #ccc;
    padding: 0.5rem;
    margin: 0.5rem 0;
    border-radius: 4px;
  }
  h3 {
    margin: 0;
  }
  p {
    margin: 0.5rem 0;
  }
  small {
    color: #666;
  }
</style>

<ul>
  {#each DiscussionTopics as a}
    <li class="announcement">
      <h3>{a.title}</h3>
      <p>{a.message}</p>
      <small>Posted by {a.author.name} on {new Date(a.posted_at).toLocaleString()}</small>
    </li>
  {/each}
</ul>
