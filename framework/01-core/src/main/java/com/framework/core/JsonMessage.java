package com.framework.core;

import com.framework.utils.Generator;

import java.io.IOException;

/**
 * Created by @panyao on 2017/8/10.
 */
final class JsonMessage implements MessageBody {

    private final BusinessMessage message;

    JsonMessage(BusinessMessage message) {
        this.message = message;
    }

    public byte[] getBytes() throws IOException {
        return Generator.getJsonHolder().writeValueAsBytes(message);
    }

}
