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
    private int portfolioId;
    private int userId;
    private int cryptoId;
    private double amount;
}