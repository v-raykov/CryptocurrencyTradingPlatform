package com.viktor.cryptocurrency.trading.platform.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private int transactionId;
    private int userId;
    private int cryptoId;
    private double amount;
    private double priceAtTransaction;
    private TransactionType transactionType;
    private Timestamp transactionDate;
}