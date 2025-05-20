package com.viktor.cryptocurrency.trading.platform.client.kraken;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viktor.cryptocurrency.trading.platform.model.kraken.SocketResponse;
import com.viktor.cryptocurrency.trading.platform.model.kraken.event.InstrumentReconnectionEvent;
import com.viktor.cryptocurrency.trading.platform.model.kraken.ticker.TickerSubscriptionRequest;
import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Crypto;
import com.viktor.cryptocurrency.trading.platform.web.service.CryptoService;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

@Component
public class TickerClient extends WebSocketClient {
    private static final Logger logger = LoggerFactory.getLogger(TickerClient.class);
    private final ObjectMapper objectMapper;
    private final CryptoService dataService;
    private final ApplicationEventPublisher publisher;

    private final List<String> pairs;

    public TickerClient(URI socketUri, ObjectMapper objectMapper, CryptoService dataService, ApplicationEventPublisher publisher, List<String> pairs) {
        super(socketUri);
        this.objectMapper = objectMapper;
        this.dataService = dataService;
        this.publisher = publisher;
        this.pairs = pairs;
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
        if (code != 1000) {
            logger.warn("WebSocket closed abnormally. Attempting reconnection...");
            publisher.publishEvent(new InstrumentReconnectionEvent());
        }
    }

    @Override
    public void onError(Exception ex) {
        logger.error("WebSocket error occurred: {}", ex.getMessage(), ex);
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

    private void subscribeToPairs() {
        try {
            send(createSubscriptionMessage());
            logger.info("Subscription request sent.");
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize subscription request", e);
        }
    }

    private String createSubscriptionMessage() throws JsonProcessingException {
        return objectMapper.writeValueAsString(new TickerSubscriptionRequest(pairs));
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
}