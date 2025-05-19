package com.viktor.cryptocurrency.trading.platform.web.controller;

import com.viktor.cryptocurrency.trading.platform.model.kraken.ticker.TickerData;
import com.viktor.cryptocurrency.trading.platform.web.service.KrakenDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class KrakenController {
    private final KrakenDataService dataService;

    @GetMapping("/websocket-data")
    public List<TickerData> getWebSocketData() {
        return dataService.getData();
    }
}
