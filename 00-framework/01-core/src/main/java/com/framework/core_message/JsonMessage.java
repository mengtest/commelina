package com.framework.core_message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.framework.utils.Generator;

/**
 * Created by @panyao on 2017/8/10.
 */
final class JsonMessage implements MessageBus {

    private final BusinessMessage message;

    JsonMessage(BusinessMessage message) {
        this.message = message;
    }

    public byte[] getBytes() {
        try {
            return Generator.getJsonHolder().writeValueAsBytes(this.message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public MessageBus.BusinessProtocol getBp() {
        return MessageBus.BusinessProtocol.JSON;
    }

}
