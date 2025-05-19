package com.viktor.cryptocurrency.trading.platform.web.service;

import com.viktor.cryptocurrency.trading.platform.model.kraken.ticker.TickerData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KrakenDataService {
    private final Map<String, TickerData> data = new HashMap<>();

    public void saveData(TickerData tickerData) {
        data.put(tickerData.getSymbol(), tickerData);
    }

    public List<TickerData> getData() {
        return new ArrayList<>(data.values());
    }
}
