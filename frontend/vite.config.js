import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';

const backendUrl = process.env.VITE_BACKEND_URL || 'http://localhost:8080';

export default defineConfig({
	plugins: [sveltekit()],
	define: {
		global: {}
	},
	server: {
		host: '0.0.0.0',
		port: 5173,
		proxy: {
			'/api': {
				target: backendUrl,
				changeOrigin: true,
				rewrite: path => path.replace(/^\/api/, ''),
			},
		},

	}
});
