<script>
    import { goto } from '$app/navigation';

    let username = '';
    let password = '';
    let confirmPassword = '';
    let errorMessage = '';

    async function handleRegister() {
        if (password !== confirmPassword) {
            errorMessage = 'Passwords do not match.';
            return;
        }

        const response = await fetch('http://localhost:8080/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }),
        });

        if (response.ok) {
            const data = await response.json();
            await goto('/login');
        } else {
            const error = await response.json();
            errorMessage = error.message || 'Registration failed. Please try again.';
        }
    }
</script>

<h1>Register</h1>

<form on:submit|preventDefault={handleRegister}>
    <label for="username">Username</label>
    <input type="text" id="username" bind:value={username} required />

    <label for="password">Password</label>
    <input type="password" id="password" bind:value={password} required />

    <label for="confirmPassword">Confirm Password</label>
    <input type="password" id="confirmPassword" bind:value={confirmPassword} required />

    <button type="submit">Register</button>
</form>

{#if errorMessage}
    <p class="error">{errorMessage}</p>
{/if}

<p>Already have an account? <a href="/login">Login here</a></p>
