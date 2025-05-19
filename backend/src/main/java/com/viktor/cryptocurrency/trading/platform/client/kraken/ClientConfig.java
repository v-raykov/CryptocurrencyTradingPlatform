package com.viktor.cryptocurrency.trading.platform.client.kraken;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class ClientConfig {
    @Bean
    public URI socketUri() {
        return URI.create("wss://ws.kraken.com/v2");
    }

    @Bean
    public String currencySuffix() {
        return "EUR";
    }
}
