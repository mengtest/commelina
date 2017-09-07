package com.framework.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.framework.utils.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by @panyao on 2017/8/10.
 */
final class JsonMessage implements MessageBus {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonMessage.class);

    private final BusinessMessage message;

    JsonMessage(BusinessMessage message) {
        this.message = message;
    }

    public byte[] getBytes() {
        try {
            return Generator.getJsonHolder().writeValueAsBytes(this.message);
        } catch (JsonProcessingException e) {
            LOGGER.error("{}", e);
            return null;
        }
    }

    public MessageBus.BusinessProtocol getBp() {
        return MessageBus.BusinessProtocol.JSON;
    }

}
