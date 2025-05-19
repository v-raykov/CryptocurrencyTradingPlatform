package com.viktor.cryptocurrency.trading.platform.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Portfolio {
    private long portfolioId;
    private long userId;
    private long cryptoId;
    private double amount;
}