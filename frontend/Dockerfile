FROM node:18

WORKDIR /app
COPY package*.json ./
RUN npm install

COPY . .

COPY ./wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh

EXPOSE 5173

ENTRYPOINT ["./wait-for-it.sh", "crypto-platform-backend:8080", "--", "npm", "run", "dev"]
