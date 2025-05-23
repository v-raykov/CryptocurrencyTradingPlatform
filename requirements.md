# Backend Dependencies Summary

**Core Framework & Web:**
- `spring-boot-starter-web` — Spring MVC web application support  
- `spring-boot-starter-webflux` — Reactive, non-blocking web framework  
- `spring-boot-starter-security` — Spring Security for authentication and authorization  
- `spring-boot-starter-jdbc` — JDBC support for database interaction  
- `spring-boot-starter-websocket` — WebSocket support  

**Database:**
- `mysql-connector-j` (runtime) — MySQL JDBC driver  

**JSON & Data Processing:**
- `jackson-datatype-jsr310` — Jackson module for Java 8 Date/Time API support  

**Authentication & JWT:**
- `jjwt-api` — JSON Web Token API  
- `jjwt-impl` (runtime) — JWT implementation  
- `jjwt-jackson` (runtime) — Jackson integration for JWT  

**WebSocket Support:**
- `Java-WebSocket` — WebSocket client and server implementation  

**Testing:**
- `spring-boot-starter-test` (test) — Testing framework (JUnit, Mockito, etc.)  
- `spring-security-test` (test) — Spring Security testing utilities  

**Utility:**
- `lombok` (optional) — Reduces boilerplate with annotations  


# Database Overview

### MySQL 8 Relational Database

# Frontend Dependencies Summary

**Core Framework & Tooling:**
- `svelte` (v5.0.0) — Modern reactive UI framework for building fast web apps  
- `@sveltejs/kit` (v2.16.0) — Application framework for building full-featured Svelte apps  
- `vite` (v6.2.6) — Fast build tool and development server  
- `@sveltejs/vite-plugin-svelte` (v5.0.0) — Vite plugin to process Svelte files  
- `@sveltejs/adapter-auto` & `@sveltejs/adapter-node` — Deployment adapters for SvelteKit supporting various platforms including Node.js  

**Communication & Networking:**
- `axios` (v1.9.0) — Promise-based HTTP client for API requests  
- `@stomp/stompjs` (v7.1.1) & `stompjs` (v2.3.3) — STOMP protocol clients for WebSocket messaging  
- `sockjs-client` (v1.6.1) — WebSocket emulation with fallback for real-time communication  

**Development Support:**
- `@types/node` (v22.15.21) — TypeScript type definitions for Node.js  
