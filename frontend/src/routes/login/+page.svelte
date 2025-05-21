<script>
    import { goto } from '$app/navigation';

    let username = '';
    let password = '';
    let errorMessage = '';

    async function handleLogin() {
        const response = await fetch('http://localhost:8080/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }),
        });

        if (response.ok) {
            const data = await response.json();
            const token = data.token;
            localStorage.setItem('jwt', token);
            await goto('/cryptos');
        } else {
            const error = await response.json();
            errorMessage = error.message || 'Login failed. Please try again.';
        }
    }
</script>

<h1>Login</h1>

<form on:submit|preventDefault={handleLogin}>
    <label for="username">Username</label>
    <input type="text" id="username" bind:value={username} required />

    <label for="password">Password</label>
    <input type="password" id="password" bind:value={password} required />

    <button type="submit">Login</button>
</form>

{#if errorMessage}
    <p class="error">{errorMessage}</p>
{/if}

<p>Don't have an account? <a href="/register">Register here</a></p>
