<script>
    import { onMount, onDestroy } from 'svelte';
    import { cryptos, message, initializeCryptoFeed, deactivateCryptoFeed } from '$lib/stores/cryptoStore.js';
    import { fetchAllTransactions } from "$lib/api.js";

    let allTransactions = [];
    let buyTransactions = [];
    let sellTransactions = [];
    let currentView = 'buy';

    $: currentMessage = $message;
    let cryptoSymbolMap = {};
    $: if (Array.isArray($cryptos)) {
        cryptoSymbolMap = $cryptos.reduce((map, crypto) => {
            map[crypto.cryptoId] = crypto.symbol;
            return map;
        }, {});
    }

    async function fetchTransactions() {
        try {
            const response = await fetchAllTransactions();
            allTransactions = response.data;
            buyTransactions = allTransactions.filter(t => t.transactionType === 'BUY');
            sellTransactions = allTransactions.filter(t => t.transactionType === 'SELL');
            message.set('');
        } catch (err) {
            console.error('Failed to fetch transactions:', err);
            if (err.response && err.response.data && err.response.data.message) {
                message.set(`Error fetching transactions: ${err.response.data.message}`);
            } else if (err.message) {
                message.set(`Failed to fetch transactions: ${err.message}`);
            } else {
                message.set('Failed to fetch transactions. An unknown error occurred.');
            }
        }
    }

    onMount(() => {
        initializeCryptoFeed();
        fetchTransactions();
    });

    onDestroy(() => {
        deactivateCryptoFeed();
    });
</script>

<h1>Transaction History</h1>

<div>
    {#if $message}
        <div>{$message}</div>
    {/if}
</div>

<div>
    <button on:click={() => currentView = 'buy'}>Show Buy Transactions</button>
    <button on:click={() => currentView = 'sell'}>Show Sell Transactions</button>
</div>

{#if currentView === 'buy'}
    <h2>Buy Transactions</h2>
    {#if buyTransactions.length > 0}
        <table>
            <thead>
            <tr>
                <th>Date</th>
                <th>Symbol</th>
                <th>Amount</th>
                <th>Price at Transaction</th>
            </tr>
            </thead>
            <tbody>
            {#each buyTransactions as transaction (transaction.transactionDate + transaction.cryptoId)}
                <tr>
                    <td>{transaction.transactionDate}</td>
                    <td>{cryptoSymbolMap[transaction.cryptoId] || transaction.cryptoId}</td>
                    <td>{transaction.amount}</td>
                    <td>{transaction.priceAtTransaction}</td>
                </tr>
            {/each}
            </tbody>
        </table>
    {:else}
        <p>No buy transactions found.</p>
    {/if}
{:else}
    <h2>Sell Transactions</h2>
    {#if sellTransactions.length > 0}
        <table>
            <thead>
            <tr>
                <th>Date</th>
                <th>Symbol</th>
                <th>Amount</th>
                <th>Price at Transaction</th>
                <th>Profit/Loss</th>
            </tr>
            </thead>
            <tbody>
            {#each sellTransactions as transaction (transaction.transactionDate + transaction.cryptoId)}
                <tr>
                    <td>{transaction.transactionDate}</td>
                    <td>{cryptoSymbolMap[transaction.cryptoId] || transaction.cryptoId}</td>
                    <td>{transaction.amount}</td>
                    <td>{transaction.priceAtTransaction}</td>
                    <td>{transaction.profitLoss}</td>
                </tr>
            {/each}
            </tbody>
        </table>
    {:else}
        <p>No sell transactions found.</p>
    {/if}
{/if}
