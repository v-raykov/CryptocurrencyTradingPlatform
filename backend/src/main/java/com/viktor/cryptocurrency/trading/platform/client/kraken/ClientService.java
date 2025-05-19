package com.viktor.cryptocurrency.trading.platform.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viktor.cryptocurrency.trading.platform.client.kraken.InstrumentClient;
import com.viktor.cryptocurrency.trading.platform.client.kraken.TickerClient;
import com.viktor.cryptocurrency.trading.platform.web.service.CryptoService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.context.ApplicationEventPublisher;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher publisher;
    private final CryptoService cryptoService;
    private final URI socketUri;
    private final String currencySuffix;

    private InstrumentClient instrumentClient;
    private TickerClient tickerClient;

    @PostConstruct
    public void init() {
        startWebSocketConnections();
    }

    public void startWebSocketConnections() {
        instrumentClient = new InstrumentClient(socketUri, objectMapper, publisher, currencySuffix);
        tickerClient = new TickerClient(socketUri, objectMapper, cryptoService);

        startInstrumentClient();
        startTickerClient();
    }

    private void startInstrumentClient() {
        if (!instrumentClient.isOpen()) {
            instrumentClient.connect();
        }
    }

    private void startTickerClient() {
        if (!tickerClient.isOpen()) {
            tickerClient.connect();
        }
    }

}
