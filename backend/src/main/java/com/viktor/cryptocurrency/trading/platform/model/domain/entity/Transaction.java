package com.viktor.cryptocurrency.trading.platform.model.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


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
    private BigDecimal profitLoss;
    private TransactionType transactionType;
    private String transactionDate;

    public Transaction(long userId, long cryptoId, BigDecimal amount, BigDecimal priceAtTransaction, TransactionType transactionType) {
        this.userId = userId;
        this.cryptoId = cryptoId;
        this.amount = amount;
        this.priceAtTransaction = priceAtTransaction;
        this.transactionType = transactionType;
        transactionDate = LocalDateTime.now().toString();
    }

    public enum TransactionType {
        BUY,
        SELL
    }

}