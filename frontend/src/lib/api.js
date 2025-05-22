import axios from 'axios';

const isServer = typeof window === 'undefined';

const backendUrl = import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080';

const api = axios.create({
    baseURL: isServer
        ? backendUrl
        : '/api',
    headers: {
        'Content-Type': 'application/json',
    }
});

export async function register(username, password) {
    return await api.post('/register', { username, password });
}

export async function login(username, password) {
    return await api.post('/login', {username, password});
}

export const getJWT = () => localStorage.getItem('jwt');