<script lang="ts">
    import config from "../config";
    import {onMount} from "svelte";
    import {redirect} from "../ts_modules/routing";
    import type {UUID} from "node:crypto";

    interface Institution {fullName: string, shortName: string, apiUrl: string;}
    interface CanvasToken {user: User, institution: Institution, token: string}
    interface User {id: UUID}

    let user: User = null;
    let tokens: CanvasToken[] = [];


    async function getTokensForUser(): Promise<CanvasToken[]> {
        const res = await fetch(`${config.API_BASE_URL}/api/tokens/user`,
            {
                method: "GET",
                signal: AbortSignal.timeout(10000),
                headers: {
                    'content-type': 'application/json'
                }, body: JSON.stringify(user)
            });
        return res.json();
    }


    onMount(async () => {
        try {
            tokens = await getTokensForUser();
        } catch (e) {
            console.error("Failed to load tokens", e);
        }
    })
</script>

<main>
    <h2>All tokens</h2>
    {#if tokens.length === 0}
        <p>There are no tokens registered yet.</p>
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
            {#each tokens as t}
                <tr>
                    <td>{t.institution.fullName}</td>
                    <td>{t.token}</td>
                </tr>
            {/each}
            </tbody>
        </table>
    {/if}
    <button onclick={() => redirect("settings/add_token")}>Add new institution</button>
</main>