<script>
    import {goto} from '$app/navigation';
    import {register} from "$lib/api.js";
    import '../app.css';

    let username = '';
    let password = '';
    let confirmPassword = '';
    let errorMessage = '';

    async function handleRegister() {
        if (password !== confirmPassword) {
            errorMessage = 'Passwords do not match.';
            return;
        }

        const response = await register(username, password);

        if (response.status === 201) {
            await goto('/core/login');
        } else {
            errorMessage = 'Registration failed. Please try again.';
        }
    }
</script>

<h1 style="text-align: center;">Register</h1>

<div style="display: flex; justify-content: center; align-items: center; min-height: 80vh; flex-direction: column;">
    <form on:submit|preventDefault={handleRegister} style="display: flex; flex-direction: column; gap: 10px; width: 300px;">
        <label for="username">Username</label>
        <input type="text" id="username" bind:value={username} required/>

        <label for="password">Password</label>
        <input type="password" id="password" bind:value={password} required/>

        <label for="confirmPassword">Confirm Password</label>
        <input type="password" id="confirmPassword" bind:value={confirmPassword} required/>

        <button type="submit">Register</button>
    </form>

    {#if errorMessage}
        <p class="error">{errorMessage}</p>
    {/if}

    <p style="margin-top: 1rem;">Already have an account? <a href="/login">Login here</a></p>
</div>
