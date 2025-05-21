<script>
    import { onMount, onDestroy } from 'svelte';
    import { Client } from '@stomp/stompjs';

    let cryptos = [];
    let connectionStatus = 'Disconnected';
    let errorMessage = '';
    let stompClient = null;

    onMount(() => {
        const token = localStorage.getItem('jwt');
        if (!token) {
            window.location.href = '/login';
            return;
        }

        stompClient = new Client({
            brokerURL: 'ws://localhost:8080/ws',
            connectHeaders: {
                Authorization: `Bearer ${token}`
            },
            debug: (str) => {
                console.log('STOMP:', str);
                connectionStatus = str.includes('ERROR') ? 'Error' : str;
            },
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
            onConnect: (frame) => {
                console.log('Connected:', frame);
                connectionStatus = 'Connected';

                // Subscribe to updates
                stompClient.subscribe('/topic/cryptos', (message) => {
                    console.log('Received:', message.body);
                    cryptos = JSON.parse(message.body);
                    cryptos = [...cryptos];
                });

                // Request initial data
                stompClient.publish({
                    destination: '/app/crypto',
                    body: JSON.stringify({})
                });
            },
            onDisconnect: (frame) => {
                console.log('Disconnected:', frame);
                connectionStatus = 'Disconnected';
            },
            onStompError: (frame) => {
                console.error('STOMP Error:', frame);
                errorMessage = frame.headers?.message || 'STOMP protocol error';
                connectionStatus = 'Error';
            },
            onWebSocketError: (event) => {
                console.error('WebSocket Error:', event);
                errorMessage = 'WebSocket connection failed';
                connectionStatus = 'WS Error';
            }
        });

        stompClient.activate();

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
    Connection: <span class:connected={connectionStatus === 'Connected'}
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
