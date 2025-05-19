package com.viktor.cryptocurrency.trading.platform.model.kraken.instrument;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class InstrumentData {
    private List<Pair> pairs;
}
