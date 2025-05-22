package com.viktor.cryptocurrency.trading.platform.model.domain.event;

import com.viktor.cryptocurrency.trading.platform.model.domain.entity.Transaction;

public record TransactionApprovedEvent(Transaction transaction) {
}
