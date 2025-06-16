# Cryptocurrency Trading Platform

## Description

This project is a cryptocurrency trading platform built from three main components:

- **Backend** – Java service that implements the business logic and API.
- **Frontend** – SvelteKit application that provides the user interface.
- **MySQL database** – stores the information needed for the application to function. The database configuration is in the `/db` directory.

The project is containerized with Docker and managed using Docker Compose.

## Project Structure

/backend/ # Java backend code and Dockerfile
/frontend/ # SvelteKit frontend code and Dockerfile
/db/ # MySQL database configuration and initialization
/docker-compose.yml # Docker Compose file to start all services
README.md # Project Documentation

## How to build and run containers

### 1. Build the Docker images:

```bash
docker build -t username/cryptotrading-backend:latest ./backend
docker build -t username/cryptotrading-frontend:latest ./frontend
```

### 2. Start the project with Docker Compose:
```bash
docker-compose up -d
```

- This starts the three services — backend, frontend, and MySQL database — in separate containers and connects them via a Docker network.
### How each component works
- Backend (Java)
  - Responsible for the platform logic and provides a REST API for the frontend. The connection to the MySQL database is made through the hostname db defined in docker-compose.yml.

- Frontend (SvelteKit)
  - Provides a user interface that consumes the backend API and visualizes the data.

- MySQL database
  - Stores user information, commercial data, and more. It is initialized with configuration and possible initial data from the /db directory.

3. Communication between services

- The services communicate with each other through the Docker network defined in docker-compose.yml. The backend service connects to the database through the hostname db. The frontend service consumes the API of the backend service using the hostname corresponding to the name of the container in Docker Compose.


## How to run
  - simply run docker-compose up --build from the root of the project
  - after the Spring application has started wait for a message like
      - `WebSocketSession[0 current WS(0)-HttpStream(0)-HttpPoll(0), 0 total, 0 closed abnormally (0 connect failure, 0 send limit, 0 transport error)], stompSubProtocol[processed CONNECT(0)-CONNECTED(0)-DISCONNECT(0)], stompBrokerRelay[null], inboundChannel[pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 0], outboundChannel[pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 0], sockJsScheduler[pool size = 12, active threads = 1, queued tasks = 1, completed tasks = 12]`
  - you can now open the frontend in the browser at [http://localhost:5173](http://localhost:5173)

## If you'd like to run without docker
  - create a MySQL database using the DDL script found in the database folder
  - add the database configuration to application.properties
  - add an entry called `JWT_SECRET_KEY` holding a random Base64 key
  - run `mvn clean install` inside the backend directory
  - run `npm install` inside the frontend directory
  - start the database, backend and frontend sequentially 
