<script lang="ts">
    import config from "../config";
    import {redirect} from "../ts_modules/routing";
    import {onMount} from "svelte";
    import {type User, get_logged_in_user, jwt_token_header} from "../ts_modules/api";

    interface Institution {fullName: string, shortName: string, apiUrl: string;}

    let loading: boolean = false;
    let institution: Institution = null;
    let institutions: Institution[] = [];
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

    async function getInstitutions(): Promise<Institution[]> {
        const res = await fetch(`${config.API_BASE_URL}/api/institutions`,
            {
                method: "GET",
                signal: AbortSignal.timeout(10000),
                headers: jwt_token_header()
            });
        return res.json();
    }



    async function addToken(event: Event) {
        event.preventDefault()

        if (!institution || !token) {
            message = "A field is missing. Please fill out all required fields.";
            return;
        }

        try {
            loading = true;
            const res = await fetch(`${config.API_BASE_URL}/api/tokens`,
                {
                    method: "POST",
                    signal: AbortSignal.timeout(10000),
                    headers: jwt_token_header(),
                    body: JSON.stringify(wrapToken(user, institution, token))
                }
            );

            console.log(res);

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

    onMount(async () => {
        user = get_logged_in_user();
        try {
            institutions = await getInstitutions();
        } catch (e) {
            console.error("Failed to load tokens", e);
        }
    })
</script>

<main>
    <h2>Add new Canvas token</h2>
    <form on:submit={addToken}>
        <label for="institution">Institution</label>
        <select
                id="institution"
                required
                bind:value={institution}
        >
            {#each institutions as i}
                <option value={i}>{i.fullName}</option>
            {/each}
        </select>

        <label for="token">Token</label>
        <input
                id="token"
                type="text"
                required
                bind:value={token}
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