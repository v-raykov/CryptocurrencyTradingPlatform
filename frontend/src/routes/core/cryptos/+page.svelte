<script>
    import {onMount, onDestroy} from 'svelte';
    import {
        cryptos,
        message,
        initializeCryptoFeed,
        deactivateCryptoFeed
    } from '$lib/stores/cryptoStore.js';
    import {buyCrypto, fetchBalance} from "$lib/api.js";
    import {balance} from "$lib/stores/balanceStore.js";

    let buyAmounts = {};

    $: currentCryptos = $cryptos;
    $: currentMessage = $message;

    async function handleBuy(cryptoId, symbol) {
        const amount = buyAmounts[symbol];
        if (amount <= 0) {
            message.set('Please enter a positive amount to buy.');
            return;
        }

        message.set('');

        try {
            await buyCrypto(cryptoId, amount);
            balance.set(await fetchBalance());
            message.set(`Successfully bought ${amount} of ${symbol}!`);
            buyAmounts[symbol] = 0;
        } catch (err) {
            if (err.response && err.response.data && err.response.data.message) {
                message.set(`Error: ${err.response.data.message}`);
            } else if (err.message) {
                message.set(`Failed to buy ${symbol}: ${err.message}`);
            } else {
                message.set(`Failed to buy ${symbol}. An unknown error occurred.`);
            }
        }
    }

    onMount(() => {
        initializeCryptoFeed();
    });

    onDestroy(() => {
        deactivateCryptoFeed();
    });
</script>

<h1>Cryptos</h1>

<div>
    {#if $message}
        <div>{$message}</div>
    {/if}
</div>

<table>
    <thead>
    <tr>
        <th>Symbol</th>
        <th>Bid</th>
        <th>Ask</th>
        <th>Last</th>
        <th>Volume</th>
        <th>Low</th>
        <th>High</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    {#each $cryptos as crypto (crypto.symbol)}
        <tr>
            <td>{crypto.symbol}</td>
            <td>{crypto.bid}</td>
            <td>{crypto.ask}</td>
            <td>{crypto.last}</td>
            <td>{crypto.volume}</td>
            <td>{crypto.low}</td>
            <td>{crypto.high}</td>
            <td>
                <div>
                    <input
                            type="number"
                            min="0"
                            step="any"
                            placeholder="Amount"
                            bind:value={buyAmounts[crypto.symbol]}
                            on:input={() => {
                            buyAmounts[crypto.symbol] = parseFloat(buyAmounts[crypto.symbol]) || 0;
                        }}
                    />
                    <button on:click={() => handleBuy(crypto.cryptoId, crypto.symbol)}>Buy</button>
                </div>
            </td>
        </tr>
    {:else}
        <tr>
            <td colspan="8">Loading crypto data...</td>
        </tr>
    {/each}
    </tbody>
</table>
