<script lang="ts">
    import config from "../config";
    import {redirect} from "../ts_modules/routing";
    import {jwt_token_header} from "../ts_modules/api";

    let loading: boolean = false;
    let shortName: string = "";
    let fullName: string = "";
    let apiUrl: string = "";
    let message: string = "";
    let csrfToken: string = "";

    async function addInstitution(event: Event) {
        event.preventDefault()

        if (!shortName || !fullName || !apiUrl) {
            message = "A field is missing. Please fill out all required fields.";
            return;
        }

        try {

            loading = true;
            const res = await fetch(`${config.API_BASE_URL}/api/institutions`,
                {
                    method: "POST",
                    signal: AbortSignal.timeout(10000),
                    headers: {
                        'Content-Type': 'application/json'
                    }, body: JSON.stringify({fullName, shortName, apiUrl})
                }
            );

            const data = await res.json();

            if (res.status == 201) {
                message = data.message || "Successfully added institution!";

                setTimeout(() => {
                    redirect("admin/settings");
                }, 1500);
            } else {
                message = data.message || "Status " + res.status;
                loading = false;
            }
        } catch(err) {
            message = "Issue reaching server | " + err
        } finally {
            loading = false;
        }
    }
</script>

<main>
    <h2>Add new institution</h2>
    <form on:submit={addInstitution}>
        <label for="fullName">Full Name</label>
        <input
            id="fullName"
            type="text"
            bind:value={fullName}
            required
            minlength="3"
            autocomplete="off"
        />

        <label for="shortName">Short Name</label>
        <input
                id="shortName"
                type="text"
                bind:value={shortName}
                required
                autocomplete="off"
        />

        <label for="apiUrl">Api Url</label>
        <input
                id="apiUrl"
                type="text"
                bind:value={apiUrl}
                required
                minlength="3"
                autocomplete="off"
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