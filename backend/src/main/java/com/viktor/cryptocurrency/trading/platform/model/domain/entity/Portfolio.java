package com.viktor.cryptocurrency.trading.platform.model.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Portfolio {
    private long portfolioId;
    private long userId;
    private long cryptoId;
    private BigDecimal amount;
}