package com.viktor.cryptocurrency.trading.platform.model.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private long transactionId;
    private long userId;
    private long cryptoId;
    private BigDecimal amount;
    private BigDecimal priceAtTransaction;
    private TransactionType transactionType;
    private Timestamp transactionDate;

    public Transaction(long userId, long cryptoId, BigDecimal amount, BigDecimal priceAtTransaction, TransactionType transactionType) {
        this.userId = userId;
        this.cryptoId = cryptoId;
        this.amount = amount;
        this.priceAtTransaction = priceAtTransaction;
        this.transactionType = transactionType;
    }

    public enum TransactionType {
        BUY,
        SELL
    }

}