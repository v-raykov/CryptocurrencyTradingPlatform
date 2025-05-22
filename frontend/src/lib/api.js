import axios from 'axios';
import {goto} from "$app/navigation";

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

export async function logout() {
    localStorage.removeItem('jwt');
    await goto('/login');
}

export const getJWT = () => localStorage.getItem('jwt');

export async function buyCrypto(cryptoId, amount) {
    return await api.post(`/crypto/${cryptoId}/buy?amount=${amount}`, null,{
        headers: {
            Authorization: `Bearer ${getJWT()}`
        }
    });
}

export async function sellCrypto(cryptoId, amount) {
    return await api.post(`/crypto/${cryptoId}/sell?amount=${amount}`, null,{
        headers: {
            Authorization: `Bearer ${getJWT()}`
        }
    });
}

export async function fetchAllPortfolios() {
    return await api.get('/user/portfolio', {
        headers: {
            Authorization: `Bearer ${getJWT()}`
        }
    });
}

export async function fetchBalance() {
    const res = await api.get('/user/balance', {
        headers: {
            Authorization: `Bearer ${getJWT()}`
        }
    });
    return res.data.balance;
}

export async function updateBalance(amount) {
    return await api.put(`/user/balance/update?amount=${amount}`, null, {
        headers: {
            Authorization: `Bearer ${getJWT()}`
        }
    });
}

export async function resetBalance() {
    return await api.put('/user/balance/reset', null,{
        headers: {
            Authorization: `Bearer ${getJWT()}`
        }
    })
}

export async function fetchAllTransactions() {
    return await api.get('/user/transaction-history', {
        headers: {
            Authorization: `Bearer ${getJWT()}`
        }
    })
}