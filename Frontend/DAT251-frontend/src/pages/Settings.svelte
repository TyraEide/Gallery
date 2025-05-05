<script lang="ts">
    import config from "../config";
    import {onMount} from "svelte";
    import {redirect} from "../ts_modules/routing";
    import {type User, get_logged_in_user, jwt_token_header} from "../ts_modules/api";

    interface Institution {fullName: string, shortName: string, apiUrl: string;}
    interface CanvasToken {user: User, institution: Institution, token: string}

    let user: User = null;
    let tokens: CanvasToken[] = [];

    async function getTokensForUser(): Promise<CanvasToken[]> {
        let id = user.id;
        try {
            const res = await fetch(`${config.API_BASE_URL}/api/tokens/user/${id}`,
                {
                    method: "GET",
                    signal: AbortSignal.timeout(10000),
                    headers: jwt_token_header()
                });

            if (res.ok) {
                return res.json();
            } else {
                return [];
            }

        } catch (e) {
            console.log("Failed to fetch tokens for user: ", e);
        }
    }


    onMount(async () => {
        user = get_logged_in_user();
        try {
            tokens = await getTokensForUser();
        } catch (e) {
            console.error("Failed to load tokens", e);
        }

        if(user == null){
            redirect("login")
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
    <button onclick={() => redirect("settings/add_token")}>Add new canvas token</button>
</main>