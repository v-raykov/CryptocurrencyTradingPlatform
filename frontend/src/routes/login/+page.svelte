<script>
    import {goto} from '$app/navigation';
    import {login} from "$lib/api.js";

    let username = '';
    let password = '';
    let errorMessage = '';

    async function handleLogin() {
        const response = await login(username, password);
        if (response.status === 200) {
            localStorage.setItem('jwt', response.data.token);
            await goto('/cryptos');
        } else {
            errorMessage = 'Login failed. Please try again.';
        }
    }
</script>

<h1>Login</h1>

<form on:submit|preventDefault={handleLogin}>
    <label for="username">Username</label>
    <input type="text" id="username" bind:value={username} required/>

    <label for="password">Password</label>
    <input type="password" id="password" bind:value={password} required/>

    <button type="submit">Login</button>
</form>

{#if errorMessage}
    <p class="error">{errorMessage}</p>
{/if}

<p>Don't have an account? <a href="/register">Register here</a></p>
