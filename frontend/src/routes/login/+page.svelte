<script>
    import {goto} from '$app/navigation';
    import {login} from "$lib/api.js";
    import '../app.css';

    let username = '';
    let password = '';
    let errorMessage = '';

    async function handleLogin() {
        const response = await login(username, password);
        if (response.status === 200) {
            localStorage.setItem('jwt', response.data.token);
            await goto('/core/cryptos');
        } else {
            errorMessage = 'Login failed. Please try again.';
        }
    }
</script>

<h1 style="text-align: center;">Login</h1>

<div style="display: flex; justify-content: center; align-items: center; min-height: 80vh; flex-direction: column;">
    <form on:submit|preventDefault={handleLogin} style="display: flex; flex-direction: column; gap: 10px; width: 300px;">
        <label for="username">Username</label>
        <input type="text" id="username" bind:value={username} required/>

        <label for="password">Password</label>
        <input type="password" id="password" bind:value={password} required/>

        <button type="submit">Login</button>
    </form>

    {#if errorMessage}
        <p class="error">{errorMessage}</p>
    {/if}

    <p style="margin-top: 1rem;">Don't have an account? <a href="/register">Register here</a></p>
</div>
