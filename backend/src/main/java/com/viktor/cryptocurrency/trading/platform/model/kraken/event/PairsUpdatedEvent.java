package com.viktor.cryptocurrency.trading.platform.model.kraken.event;

import java.util.List;

public record PairsUpdatedEvent(List<String> pairs) {
}
