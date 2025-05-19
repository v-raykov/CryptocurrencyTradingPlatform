package com.viktor.cryptocurrency.trading.platform.model.kraken.instrument;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pair {
    private String symbol;
}
