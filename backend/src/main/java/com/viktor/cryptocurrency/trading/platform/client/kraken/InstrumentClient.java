package com.viktor.cryptocurrency.trading.platform.client.kraken;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viktor.cryptocurrency.trading.platform.model.kraken.SocketResponse;
import com.viktor.cryptocurrency.trading.platform.model.kraken.event.PairsUpdatedEvent;
import com.viktor.cryptocurrency.trading.platform.model.kraken.instrument.InstrumentData;
import com.viktor.cryptocurrency.trading.platform.model.kraken.instrument.InstrumentSubscriptionRequest;
import com.viktor.cryptocurrency.trading.platform.model.kraken.instrument.Pair;
import jakarta.annotation.PostConstruct;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InstrumentClient extends WebSocketClient {
    private static final Logger logger = LoggerFactory.getLogger(InstrumentClient.class);
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher publisher;
    private final String currencySuffix;

    public InstrumentClient(URI socketUri, ObjectMapper objectMapper, ApplicationEventPublisher publisher, String currencySuffix) {
        super(socketUri);
        this.objectMapper = objectMapper;
        this.publisher = publisher;
        this.currencySuffix = currencySuffix;
    }

    @PostConstruct
    public void connectToWebSocket() {
        if (!isOpen()) {
            logger.info("Attempting to connect to Instrument WebSocket...");
            connectWithRetry();
        }
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        logger.info("Instrument WebSocket connection established.");
        subscribeToInstrumentPairs();
    }

    @Override
    public void onMessage(String message) {
        handleMessage(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        logger.info("WebSocket closed. Code: {}, Reason: {}, Remote: {}", code, reason, remote);
        if (code != 1000) {
            logger.warn("WebSocket closed abnormally. Attempting reconnection...");
            reconnectWithRetry();
        }
    }

    @Override
    public void onError(Exception ex) {
        logger.error("WebSocket error occurred: {}", ex.getMessage(), ex);
    }

    private void handleMessage(String message) {
        SocketResponse<InstrumentData> response = parseMessage(message);
        if (response != null) {
            List<String> topPairs = getTopPairs(response.getData().getPairs());
            publisher.publishEvent(new PairsUpdatedEvent(topPairs));
        }
    }

    private List<String> getTopPairs(List<Pair> pairs) {
        return pairs.stream()
                .filter(pair -> pair.getSymbol().endsWith("/" + currencySuffix))
                .limit(20)
                .map(Pair::getSymbol)
                .collect(Collectors.toList());
    }

    private void subscribeToInstrumentPairs() {
        try {
            String message = objectMapper.writeValueAsString(new InstrumentSubscriptionRequest());
            send(message);
            logger.info("Subscription request sent: {}", message);
        } catch (Exception e) {
            logger.error("Failed to subscribe to pairs: {}", e.getMessage(), e);
        }
    }

    private SocketResponse<InstrumentData> parseMessage(String message) {
        try {
            return objectMapper.readValue(message, objectMapper.getTypeFactory()
                    .constructParametricType(SocketResponse.class, InstrumentData.class));
        } catch (Exception e) {
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

    private void reconnectWithRetry() {
        logger.info("Attempting to reconnect...");
        connectWithRetry();
    }
}
