import { writable } from 'svelte/store';
import { createStompClient } from './stompClient.js';

export const cryptos = writable([]);
export const message = writable('');

let stompClientInstance;

export function initializeCryptoFeed() {
    if (stompClientInstance && stompClientInstance.active) {
        return;
    }

    stompClientInstance = createStompClient({
        onConnect: (frame, client) => {
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
        },
        onWebSocketError: () => {
            message.set('WebSocket connection failed. Check server status.');
        },
        setStatus: (status) => console.log(status)
    });

    stompClientInstance.activate();
}

export function deactivateCryptoFeed() {
    if (stompClientInstance && stompClientInstance.active) {
        stompClientInstance.deactivate();
    }
}
