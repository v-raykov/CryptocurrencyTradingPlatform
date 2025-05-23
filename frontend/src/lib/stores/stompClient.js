import {Client} from '@stomp/stompjs';
import {getJWT} from '$lib/api.js';
import {goto} from "$app/navigation";

export const createStompClient = ({
                                      onConnect,
                                      onError,
                                      onDisconnect,
                                      onWebSocketError,
                                      setStatus,
                                  }) => {
    const token = getJWT();
    if (!token) {
        goto('/login').then(r => {
        });
        return null;
    }

    const client = new Client({
        brokerURL: 'ws://localhost:8080/ws',
        connectHeaders: {
            Authorization: `Bearer ${token}`
        },
        debug: (str) => {
            console.log('STOMP:', str);
            setStatus(str.includes('ERROR') ? 'Error' : str);
        },
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        onConnect: (frame) => {
            setStatus('Connected');
            onConnect(frame, client);
        },
        onDisconnect: (frame) => {
            console.log('Disconnected:', frame);
            setStatus('Disconnected');
            onDisconnect?.(frame);
        },
        onStompError: (frame) => {
            console.error('STOMP Error:', frame);
            onError(frame);
        },
        onWebSocketError: (event) => {
            console.error('WebSocket Error:', event);
            onWebSocketError();
        }
    });

    return client;
};
