package com.viktor.cryptocurrency.trading.platform.model.kraken.ticker;

import com.viktor.cryptocurrency.trading.platform.model.kraken.SubscriptionRequest;
import lombok.Getter;

import java.util.List;

public class TickerSubscriptionRequest extends SubscriptionRequest {
    public TickerSubscriptionRequest(List<String> pairs) {
        setParams(new TickerParams(getChannel(), pairs));
    }

    @Override
    protected String getChannel() {
        return "ticker";
    }

    @Getter
    public static class TickerParams extends SubscriptionRequest.Params {
        private final List<String> symbol;

        public TickerParams(String channel, List<String> pairs) {
            super(channel);
            this.symbol = pairs;
        }
    }


}