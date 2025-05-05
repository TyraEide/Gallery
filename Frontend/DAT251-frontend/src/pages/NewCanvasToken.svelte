<script lang="ts">
    import config from "../config";
    import {redirect} from "../ts_modules/routing";
    import type {UUID} from "node:crypto";

    interface Institution {fullName: string, shortName: string, apiUrl: string;}
    interface User {id: UUID}
    interface CanvasToken {user: User, institution: Institution, token: string}

    let loading: boolean = false;
    let institution: Institution = null;
    let institutions: Institution[] = [];
    let canvasToken: string = "";
    let user: User = null;
    let token: string = "";
    let message: string = "";

    function wrapToken(user: User, institution: Institution, token: string) {
        return {
            user: user,
            institution: institution,
            token: token
        };
    }

    async function getUser() {}

    async function getInstitutions() {}



    async function addToken(event: Event) {
        event.preventDefault()

        if (!institution || !canvasToken) {
            message = "A field is missing. Please fill out all required fields.";
            return;
        }

        try {
            loading = true;
            const res = await fetch(`${config.API_BASE_URL}/api/tokens`,
                {
                    method: "POST",
                    signal: AbortSignal.timeout(10000),
                    headers: {
                        'content-type': 'application/json'
                    }, body: JSON.stringify(wrapToken(user, institution, token))
                }
            );

            const data = await res.json();

            if (!res.ok) {
                message =
                    data.message || "This token already exists.";
                loading = false;
            } else {
                message = data.message || "Successfully added token!";

                setTimeout(() =>
                {
                    redirect("dashboard");
                }, 1500);
            }
        } catch(err) {
            message = "Issue reaching server | " + err
        } finally {
            loading = false;
        }
    }
</script>

<main>
    <h2>Add new Canvas token</h2>
    <form on:submit={addToken}>
        <label for="institution">Institution</label>
        <select
                id="institution"
                required
        >
            {#each institutions as i}
                <option value={i.shortName}>{i.fullName}</option>
            {/each}
        </select>

        <label for="token">Token</label>
        <input
                id="token"
                type="text"
                required
        />

        <button type="submit" disabled={loading}>
            {#if loading}
                <span class="spinner"></span> Loading...
            {:else}
                Add
            {/if}
        </button>
    </form>
    <p>{message}</p>
</main>