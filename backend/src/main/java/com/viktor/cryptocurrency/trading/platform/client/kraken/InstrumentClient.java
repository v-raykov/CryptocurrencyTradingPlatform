package com.viktor.cryptocurrency.trading.platform.client.kraken;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viktor.cryptocurrency.trading.platform.model.kraken.SocketResponse;
import com.viktor.cryptocurrency.trading.platform.model.kraken.event.InstrumentReconnectionEvent;
import com.viktor.cryptocurrency.trading.platform.model.kraken.event.TickerReconnectionEvent;
import com.viktor.cryptocurrency.trading.platform.model.kraken.instrument.InstrumentData;
import com.viktor.cryptocurrency.trading.platform.model.kraken.instrument.InstrumentSubscriptionRequest;
import com.viktor.cryptocurrency.trading.platform.model.kraken.instrument.Pair;
import lombok.Getter;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class InstrumentClient extends WebSocketClient {
    private static final Logger logger = LoggerFactory.getLogger(InstrumentClient.class);
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher publisher;
    private final String currencySuffix;

    @Getter
    private List<String> pairs;

    public InstrumentClient(URI socketUri, ObjectMapper objectMapper, ApplicationEventPublisher publisher, String currencySuffix) {
        super(socketUri);
        this.objectMapper = objectMapper;
        this.publisher = publisher;
        this.currencySuffix = currencySuffix;
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
            publisher.publishEvent(new InstrumentReconnectionEvent());
        }
    }

    @Override
    public void onError(Exception ex) {
        logger.error("WebSocket error occurred: {}", ex.getMessage(), ex);
    }

    private void handleMessage(String message) {
        SocketResponse<InstrumentData> response = parseMessage(message);
        if (response != null) {
            pairs = getTopPairs(response.getData().getPairs());
            publisher.publishEvent(new TickerReconnectionEvent());
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
            send(objectMapper.writeValueAsString(new InstrumentSubscriptionRequest()));
            logger.info("Subscription request sent.");
        } catch (Exception e) {
            logger.error("Failed to subscribe to pairs.", e);
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
}
