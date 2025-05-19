package com.viktor.cryptocurrency.trading.platform.config.mapper.object.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.viktor.cryptocurrency.trading.platform.model.kraken.SocketResponse;

import java.io.IOException;

public class SocketResponseDeserializer<T> extends JsonDeserializer<SocketResponse<T>> implements ContextualDeserializer {
    private JavaType type;

    @Override
    public SocketResponse<T> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectNode node = p.getCodec().readTree(p);
        JsonNode channelNode = node.get("channel");
        if (channelNode == null || channelNode.asText().equals("heartbeat") || channelNode.asText().equals("status")) {
            return null;
        }

        JsonNode dataNode = node.get("data");
        JsonNode resultNode = dataNode.isArray() && !dataNode.isEmpty() ? dataNode.get(0) : dataNode;

        return new SocketResponse<>(((ObjectMapper) p.getCodec()).readValue(resultNode.traverse(), type));
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        SocketResponseDeserializer<T> deserializer = new SocketResponseDeserializer<>();
        deserializer.type = property != null ? property.getType().containedType(0) : ctxt.getContextualType().containedType(0);
        return deserializer;
    }
}