<script lang="ts">
  type Announcement = {
    title: string;
    message: string;
    author: { name: string };
    posted_at: string;
    is_announcement: boolean;
  };

  let announcements: Announcement[] = [
    {
      title: "Title1",
      message: "Message1",
      author: { name: "Author1" },
      posted_at: new Date().toISOString(),
      is_announcement: true
    },
    {
      title: "Title2",
      message: "Message2",
      author: { name: "Author2" },
      posted_at: new Date().toISOString(),
      is_announcement: true
    }
  ];
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
  {#each announcements as a}
    <li class="announcement">
      <h3>{a.title}</h3>
      <p>{a.message}</p>
      <small>Posted by {a.author?.name} on {new Date(a.posted_at).toLocaleString()}</small>
    </li>
  {/each}
</ul>
