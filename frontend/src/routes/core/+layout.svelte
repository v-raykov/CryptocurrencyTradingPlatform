<script>
    import '../app.css';
    import {fetchBalance, updateBalance, resetUser, logout} from '$lib/api.js';
    import {onMount} from "svelte";
    import {balance} from '$lib/stores/balanceStore.js';

    onMount(async () => {
        balance.set(await fetchBalance());
    });

    async function handleUpdate() {
        const input = prompt("Enter balance change (positive or negative):");
        if (input === null) return;

        const amount = parseFloat(input);
        if (!isNaN(amount)) {
            await updateBalance(amount)
            balance.set(await fetchBalance());
        } else {
            alert("Invalid number.");
        }
    }

    async function handleReset() {
        if (confirm("Are you sure you want to reset all your user data?")) {
            await resetUser();
            balance.set(await fetchBalance());
        }
    }

    function handleLogout() {
        logout();
    }
</script>

<header>
    <span>Cryptocurrency Trading Platform</span>
    <div class="user-controls">
        <span>Balance: {$balance.toFixed(2)}</span>
        <button on:click={handleUpdate}>Update</button>
        <button on:click={handleReset}>Reset</button>
        <button on:click={handleLogout}>Logout</button>
    </div>
</header>

<div class="container">
    <nav class="sidebar">
        <a href="/core/cryptos" class="nav-item">Cryptocurrency market</a>
        <a href="/core/portfolio" class="nav-item">Portfolio</a>
        <a href="/core/transactions-history" class="nav-item">Transactions History</a>
    </nav>

    <main class="main">
        <slot/>
    </main>
</div>