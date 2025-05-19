package com.viktor.cryptocurrency.trading.platform.model.kraken.instrument;

import com.viktor.cryptocurrency.trading.platform.model.kraken.SubscriptionRequest;


public class InstrumentSubscriptionRequest extends SubscriptionRequest {
    @Override
    protected String getChannel() {
        return "instrument";
    }
}
