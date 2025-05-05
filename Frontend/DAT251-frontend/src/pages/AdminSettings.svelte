<script lang="ts">
    import config from "../config";
    import {onMount} from "svelte";
    import {redirect} from "../ts_modules/routing";

    interface Institution {fullName: string, shortName: string, apiUrl: string;}

    async function getInstitutions(): Promise<Institution[]> {
        const res = await fetch(`${config.API_BASE_URL}/api/institutions`,
            {
                method: "GET",
                signal: AbortSignal.timeout(10000),
                headers: {
                    'content-type': 'application/json'
                }
            });
        return res.json();
    }

    let institutions: Institution[] = [];

    onMount(async () => {
        try {
            institutions = await getInstitutions();
        } catch (e) {
            console.error("Failed to load institutions", e);
        }
    })
</script>

<main>
    <h2>All institutions</h2>
    {#if institutions.length === 0}
        <p>There are no institutions registered yet.</p>
    {:else}
        <table>
        <thead>
            <tr>
                <th>Full Name</th>
                <th>Short Name</th>
                <th>Api Url</th>
            </tr>
        </thead>
        <tbody>
        {#each institutions as i}
        <tr>
            <td>{i.fullName}</td>
            <td>{i.shortName}</td>
            <td>{i.apiUrl}</td>
        </tr>
        {/each}
        </tbody>
    </table>
    {/if}
    <button onclick={() => redirect("admin/settings/add_institution")}>Add new institution</button>
</main>