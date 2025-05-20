package com.viktor.cryptocurrency.trading.platform.client.kraken;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viktor.cryptocurrency.trading.platform.model.kraken.event.InstrumentReconnectionEvent;
import com.viktor.cryptocurrency.trading.platform.model.kraken.event.TickerReconnectionEvent;
import com.viktor.cryptocurrency.trading.platform.web.service.CryptoService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final CryptoService cryptoService;
    private final URI socketUri;
    private final ObjectMapper objectMapper;
    private final String currencySuffix;
    private final ApplicationEventPublisher publisher;

    private InstrumentClient instrumentClient;
    private TickerClient tickerClient;

    @PostConstruct
    public void init() {
        startInstrumentClient();
    }

    @EventListener(InstrumentReconnectionEvent.class)
    public void onInstrumentReconnectionEvent() {
        if (instrumentClient != null) {
            instrumentClient.close();
        }
        startInstrumentClient();
    }

    @EventListener(TickerReconnectionEvent.class)
    public void onTickerReconnectionEvent() {
        if (tickerClient != null) {
            tickerClient.close();
        }
        startTickerClient();
    }

    private void startInstrumentClient() {
        instrumentClient = new InstrumentClient(socketUri, objectMapper, publisher, currencySuffix);
        instrumentClient.connect();
    }

    private void startTickerClient() {
        tickerClient = new TickerClient(socketUri, objectMapper, cryptoService, publisher, instrumentClient.getPairs());
        tickerClient.connect();
    }

}
