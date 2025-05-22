<script>
    import { onMount, onDestroy } from 'svelte';
    import { createStompClient } from './stompClient.js';
    import {buyCrypto} from "$lib/api.js";

    let cryptos = [];
    let connectionStatus = 'Disconnected';
    let message = '';
    let stompClient;

    let buyAmounts = {};

    function setStatus(status) {
        connectionStatus = status;
    }

    async function handleConnect(frame, client) {
        try {
            client.subscribe('/topic/cryptos', (message) => {
                cryptos = JSON.parse(message.body);
                cryptos.forEach(crypto => {
                    if (buyAmounts[crypto.symbol] === undefined) {
                        buyAmounts[crypto.symbol] = 0;
                    }
                });
            });

            client.publish({
                destination: '/app/crypto',
                body: JSON.stringify({})
            });

            setStatus('Connected');

        } catch (err) {
            console.error('Error during STOMP connection setup:', err);
            message = 'Failed to fetch initial crypto data or subscribe.';
            setStatus('Error');
        }
    }

    async function handleBuy(cryptoId, symbol) {
        const amount = buyAmounts[symbol];
        if (amount <= 0) {
            message = 'Please enter a positive amount to buy.';
            return;
        }

        message = ''; // Clear previous message (error or success)

        try {
            await buyCrypto(cryptoId, amount);
            console.log(`Buy request sent for ${symbol}: amount=${amount}`);
            message = `Successfully bought ${amount} of ${symbol}!`; // Success message
            buyAmounts[symbol] = 0;
        } catch (err) {
            console.error('Failed to send buy request:', err);
            if (err.response && err.response.data && err.response.data.message) {
                message = `Error: ${err.response.data.message}`;
            } else if (err.message) {
                message = `Failed to buy ${symbol}: ${err.message}`;
            } else {
                message = `Failed to buy ${symbol}. An unknown error occurred.`;
            }
        }
    }

    onMount(() => {
        stompClient = createStompClient({
            onConnect: handleConnect,
            onError: (frame) => {
                message = frame.headers?.message || 'STOMP error';
                setStatus('Error');
                console.error('STOMP Error:', frame);
            },
            onWebSocketError: (event) => {
                message = 'WebSocket connection failed. Check server status.';
                setStatus('WS Error');
                console.error('WebSocket Error:', event);
            },
            setStatus
        });

        stompClient?.activate();

        return () => {
            if (stompClient?.active) {
                stompClient.deactivate();
            }
        };
    });

    onDestroy(() => {
        if (stompClient?.active) {
            stompClient.deactivate();
        }
    });
</script>

<h1>Cryptos</h1>

<div class="status">
    Connection:
    <span class:connected={connectionStatus === 'Connected'}
          class:error={connectionStatus.includes('Error')}>
        {connectionStatus}
    </span>

    {#if message}
        <div class="error-message">{message}</div>
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
        <th>Actions</th> </tr>
    </thead>
    <tbody>
    {#each cryptos as crypto (crypto.symbol)} <tr>
        <td data-label="Symbol">{crypto.symbol}</td>
        <td data-label="Bid">{crypto.bid}</td>
        <td data-label="Ask">{crypto.ask}</td>
        <td data-label="Last">{crypto.last}</td>
        <td data-label="Volume">{crypto.volume}</td>
        <td data-label="Low">{crypto.low}</td>
        <td data-label="High">{crypto.high}</td>
        <td data-label="Actions">
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
            <td colspan="8">No crypto data available</td> </tr>
    {/each}
    </tbody>
</table>
