package com.viktor.cryptocurrency.trading.platform.model.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Crypto {
    private long cryptoId;
    private String symbol;
    private double bid;
    private double ask;
    private double last;
    private double volume;
    private double low;
    private double high;
}
