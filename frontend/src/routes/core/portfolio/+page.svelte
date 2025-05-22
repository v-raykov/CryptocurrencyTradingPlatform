<script>
    import {onMount, onDestroy} from 'svelte';
    import {
        cryptos,
        connectionStatus,
        message,
        initializeCryptoFeed,
        deactivateCryptoFeed
    } from '$lib/stores/cryptoStore.js';
    import {sellCrypto, fetchAllPortfolios, fetchBalance} from "$lib/api.js";
    import {balance} from "$lib/stores/balanceStore.js";

    let portfolios = [];

    $: displayedCryptos = $cryptos.map(crypto => {
        const portfolioEntry = portfolios.find(p => p.cryptoId === crypto.cryptoId);
        if (portfolioEntry) {
            return {...crypto, ownedAmount: portfolioEntry.amount};
        }
        return null;
    }).filter(Boolean);

    $: currentConnectionStatus = $connectionStatus;
    $: currentMessage = $message;

    let sellAmounts = {};
    $: {
        if (displayedCryptos) {
            displayedCryptos.forEach(crypto => {
                if (sellAmounts[crypto.symbol] === undefined) {
                    sellAmounts[crypto.symbol] = 0;
                }
            });
        }
    }

    async function fetchPortfolios() {
        try {
            const response = await fetchAllPortfolios();
            portfolios = response.data;
            message.set('');
        } catch (err) {
            console.error('Failed to fetch portfolios:', err);
            if (err.response && err.response.data && err.response.data.message) {
                message.set(`Error fetching portfolio: ${err.response.data.message}`);
            } else if (err.message) {
                message.set(`Failed to fetch portfolio: ${err.message}`);
            } else {
                message.set('Failed to fetch portfolio. An unknown error occurred.');
            }
        }
    }

    async function handleSell(cryptoId, symbol) {
        const amount = sellAmounts[symbol];
        if (amount <= 0) {
            message.set('Please enter a positive amount to sell.');
            return;
        }

        message.set('');

        try {
            await sellCrypto(cryptoId, amount);
            message.set(`Successfully sold ${amount} of ${symbol}!`);
            sellAmounts[symbol] = 0;
            await fetchPortfolios();
            balance.set(await fetchBalance());
        } catch (err) {
            console.error('Failed to send sell request:', err);
            if (err.response && err.response.data && err.response.data.message) {
                message.set(`Error: ${err.response.data.message}`);
            } else if (err.message) {
                message.set(`Failed to sell ${symbol}: ${err.message}`);
            } else {
                message.set(`Failed to sell ${symbol}. An unknown error occurred.`);
            }
        }
    }

    onMount(() => {
        initializeCryptoFeed();
        fetchPortfolios();
    });

    onDestroy(() => {
        deactivateCryptoFeed();
    });
</script>

<h1>Portfolio</h1>

<div>
    Connection:
    <span>
        {$connectionStatus}
    </span>

    {#if $message}
        <div>{$message}</div>
    {/if}
</div>

{#if displayedCryptos.length > 0}
    <table>
        <thead>
        <tr>
            <th>Symbol</th>
            <th>Last</th>
            <th>High</th>
            <th>Low</th>
            <th>Owned Amount</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        {#each displayedCryptos as crypto (crypto.symbol)}
            <tr>
                <td>{crypto.symbol}</td>
                <td>{crypto.last}</td>
                <td>{crypto.high}</td>
                <td>{crypto.low}</td>
                <td>{crypto.ownedAmount}</td>
                <td>
                    <div>
                        <input
                                type="number"
                                min="0"
                                step="any"
                                placeholder="Amount"
                                bind:value={sellAmounts[crypto.symbol]}
                                on:input={() => {
                                sellAmounts[crypto.symbol] = parseFloat(sellAmounts[crypto.symbol]) || 0;
                            }}
                        />
                        <button on:click={() => handleSell(crypto.cryptoId, crypto.symbol)}>Sell</button>
                    </div>
                </td>
            </tr>
        {/each}
        </tbody>
    </table>
{:else}
    <p>No crypto owned</p>
{/if}
