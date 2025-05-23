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
