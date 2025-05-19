package com.viktor.cryptocurrency.trading.platform.config.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Configuration
public class HttpClientConfig {
    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Bean
    public ThrowingFunction<String, String> sendRequest() {
        return url -> {
            URI uri = URI.create(url);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .build();
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        };
    }

    @Bean
    public URI socketUri() throws URISyntaxException {
        return new URI("wss://ws.kraken.com/v2");
    }

}
