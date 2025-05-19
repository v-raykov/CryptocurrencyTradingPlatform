package com.viktor.cryptocurrency.trading.platform.model.kraken;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class SubscriptionRequest {
    private final String method = "subscribe";
    private Params params;
    public SubscriptionRequest() {
        params = new Params(getChannel());
    }

    @Getter
    public static class Params {
        private final String channel;
        public Params(String channel) {
            this.channel = channel;
        }
    }

    protected abstract String getChannel();
}
