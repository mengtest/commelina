package org.commelina.core;

import org.commelina.utils.Generator;

import java.io.IOException;

/**
 * json 消息实现
 *
 * @author @panyao
 * @date 2017/8/10
 */
final class JsonMessage implements MessageBody {

    private final BusinessMessage message;

    JsonMessage(BusinessMessage message) {
        this.message = message;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return Generator.getJsonHolder().writeValueAsBytes(message);
    }

}
