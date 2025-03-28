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
      const response = await fetch("http://backend-API-url/announcements");
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

<ul>
  {#each DiscussionTopics as DT}
    <li class="announcement">
      <h3>{DT.title}</h3>
      <p>{DT.message}</p>
      <small>Posted by {DT.author.name} on {new Date(DT.posted_at).toLocaleString()}</small>
    </li>
  {/each}
</ul>
