package com.viktor.cryptocurrency.trading.platform.model.kraken;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.viktor.cryptocurrency.trading.platform.config.mapper.object.deserializer.SocketResponseDeserializer;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonDeserialize(using = SocketResponseDeserializer.class)
public class SocketResponse<T> {
    @JsonCreator
    public SocketResponse(@JsonProperty("data") T data) {
        this.data = data;
    }

    T data;
}
