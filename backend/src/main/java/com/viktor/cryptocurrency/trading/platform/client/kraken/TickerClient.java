package com.viktor.cryptocurrency.trading.platform.client.kraken;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viktor.cryptocurrency.trading.platform.model.kraken.SocketResponse;
import com.viktor.cryptocurrency.trading.platform.model.kraken.event.PairsUpdatedEvent;
import com.viktor.cryptocurrency.trading.platform.model.kraken.ticker.TickerSubscriptionRequest;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Crypto;
import com.viktor.cryptocurrency.trading.platform.web.service.CryptoService;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

@Component
public class TickerClient extends WebSocketClient {
    private static final Logger logger = LoggerFactory.getLogger(TickerClient.class);
    private final ObjectMapper objectMapper;
    private final CryptoService dataService;

    private List<String> pairs;

    public TickerClient(URI socketUri, ObjectMapper objectMapper, CryptoService dataService) {
        super(socketUri);
        this.objectMapper = objectMapper;
        this.dataService = dataService;
    }

    @EventListener(PairsUpdatedEvent.class)
    public void onPairsUpdated(PairsUpdatedEvent event) {
        updatePairsAndReconnect(event.pairs());
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        logger.info("WebSocket connection established.");
        subscribeToPairs();
    }

    @Override
    public void onMessage(String message) {
        processMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        logger.info("WebSocket closed. Code: {}, Reason: {}, Remote: {}", code, reason, remote);
        handleReconnection();
    }

    @Override
    public void onError(Exception ex) {
        logger.error("WebSocket error", ex);
        handleReconnection();
    }

    @Override
    public void close() {
        try {
            super.close();
            logger.info("WebSocket connection closed.");
        } catch (Exception e) {
            logger.error("Error closing WebSocket", e);
        }
    }

    private void processMessage(String message) {
        try {
            SocketResponse<Crypto> response = parseMessage(message);
            if (response != null) {
                dataService.saveCrypto(response.getData());
            }
        } catch (Exception e) {
            logger.error("Error processing message: {}", message, e);
        }
    }

    private void updatePairsAndReconnect(List<String> newPairs) {
        if (isOpen()) {
            logger.info("Closing WebSocket connection.");
            close();
        }
        this.pairs = newPairs;
        logger.info("Updated pairs: {}", pairs);
        connectWithRetry();
    }

    private void subscribeToPairs() {
        try {
            String subscriptionMessage = createSubscriptionMessage();
            send(subscriptionMessage);
            logger.info("Subscription request sent.");
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize subscription request", e);
        }
    }

    private String createSubscriptionMessage() throws JsonProcessingException {
        TickerSubscriptionRequest subscriptionRequest = new TickerSubscriptionRequest(pairs);
        return objectMapper.writeValueAsString(subscriptionRequest);
    }

    private SocketResponse<Crypto> parseMessage(String message) {
        try {
            return objectMapper.readValue(message, objectMapper.getTypeFactory()
                    .constructParametricType(SocketResponse.class, Crypto.class));
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse message: {}", message, e);
            return null;
        }
    }

    private void connectWithRetry() {
        try {
            RetryHandler.retry(this::connect);
        } catch (Exception e) {
            logger.error("WebSocket connection failed after retries.", e);
        }
    }

    private void handleReconnection() {
        logger.info("Attempting to reconnect.");
        connectWithRetry();
    }
}