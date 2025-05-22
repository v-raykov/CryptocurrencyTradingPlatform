<script>
    import { onMount, onDestroy } from 'svelte';
    import { createStompClient } from './stompClient.js';

    let cryptos = [];
    let connectionStatus = 'Disconnected';
    let errorMessage = '';
    let stompClient;

    function setStatus(status) {
        connectionStatus = status;
    }

    async function handleConnect(frame, client) {
        try {
            client.subscribe('/topic/cryptos', (message) => {
                cryptos = JSON.parse(message.body);
            });

            client.publish({
                destination: '/app/crypto',
                body: JSON.stringify({})
            });

        } catch (err) {
            console.error(err);
            errorMessage = 'Failed to fetch initial crypto data';
        }
    }

    onMount(() => {
        stompClient = createStompClient({
            onConnect: handleConnect,
            onError: (frame) => {
                errorMessage = frame.headers?.message || 'STOMP error';
                connectionStatus = 'Error';
            },
            onWebSocketError: () => {
                errorMessage = 'WebSocket connection failed';
                connectionStatus = 'WS Error';
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

    {#if errorMessage}
        <div class="error-message">{errorMessage}</div>
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
    </tr>
    </thead>
    <tbody>
    {#each cryptos as crypto}
        <tr>
            <td>{crypto.symbol}</td>
            <td>{crypto.bid}</td>
            <td>{crypto.ask}</td>
            <td>{crypto.last}</td>
            <td>{crypto.volume}</td>
            <td>{crypto.low}</td>
            <td>{crypto.high}</td>
        </tr>
    {:else}
        <tr>
            <td colspan="7">No crypto data available</td>
        </tr>
    {/each}
    </tbody>
</table>
