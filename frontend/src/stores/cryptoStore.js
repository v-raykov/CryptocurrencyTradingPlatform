import { writable } from 'svelte/store';
import { createStompClient } from './stompClient.js';

export const cryptos = writable([]);
export const connectionStatus = writable('Disconnected');
export const message = writable('');

let stompClientInstance;

export function initializeCryptoFeed() {
    if (stompClientInstance && stompClientInstance.active) {
        return;
    }

    stompClientInstance = createStompClient({
        onConnect: (frame, client) => {
            connectionStatus.set('Connected');
            message.set('');

            client.subscribe('/topic/cryptos', (msg) => {
                const receivedCryptos = JSON.parse(msg.body);
                cryptos.set(receivedCryptos);
            });

            client.publish({
                destination: '/app/crypto',
                body: JSON.stringify({})
            });
        },
        onError: (frame) => {
            message.set(frame.headers?.message || 'STOMP error');
            connectionStatus.set('Error');
        },
        onWebSocketError: (event) => {
            message.set('WebSocket connection failed. Check server status.');
            connectionStatus.set('WS Error');
        },
        setStatus: (status) => connectionStatus.set(status)
    });

    stompClientInstance.activate();
}

export function deactivateCryptoFeed() {
    if (stompClientInstance && stompClientInstance.active) {
        stompClientInstance.deactivate();
        connectionStatus.set('Disconnected');
    }
}
