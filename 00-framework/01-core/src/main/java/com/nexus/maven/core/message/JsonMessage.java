package com.nexus.maven.core.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nexus.maven.utils.Generator;

/**
 * Created by @panyao on 2017/8/10.
 */
final class JsonMessage implements MessageBus {

    private final int opCode;
    private final BusinessMessage message;

    JsonMessage(int opCode, BusinessMessage message) {
        this.opCode = opCode;
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

    public int getOpCode() {
        return opCode;
    }

    public MessageBus.BusinessProtocol getBp() {
        return MessageBus.BusinessProtocol.JSON;
    }

}
