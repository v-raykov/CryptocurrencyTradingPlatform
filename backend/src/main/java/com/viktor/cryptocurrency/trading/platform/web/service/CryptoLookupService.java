package com.viktor.cryptocurrency.trading.platform.web.service;

import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CryptoLookupService {

    private static final String BASE_URL = "https://api.coinpaprika.com/v1";
    private static final String COINS_LIST_ENDPOINT = "/coins";

    private final WebClient webClient = WebClient.create(BASE_URL);

    private final Map<String, String> symbolToNameCache = new ConcurrentHashMap<>();

    private static class Coin {
        public String symbol;
        public String name;
    }

    @PostConstruct
    public void init() {
        refreshCache().block();
    }

    @Scheduled(fixedRate = 60 * 1000)
    public Mono<Void> refreshCache() {
        return webClient.get()
                .uri(COINS_LIST_ENDPOINT)
                .retrieve()
                .bodyToFlux(Coin.class)
                .collectList()
                .doOnNext(this::populateCache)
                .then();
    }

    private void populateCache(List<Coin> coins) {
        Map<String, String> updated = coins.stream()
                .collect(Collectors.toMap(
                        coin -> coin.symbol.toLowerCase(),
                        coin -> coin.name,
                        (existing, replacement) -> existing
                ));
        symbolToNameCache.clear();
        symbolToNameCache.putAll(updated);
    }

    public Mono<String> getCryptoNameBySymbol(String symbol) {
        String name = symbolToNameCache.get(symbol.toLowerCase().split("/")[0]);
        return name != null ? Mono.just(name) : Mono.empty();
    }
}
