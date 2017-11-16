package com.commelina.core;

import com.commelina.utils.Generator;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * json 消息实现
 *
 * @author @panyao
 * @date 2017/8/10
 */
@Deprecated
final class JsonMessage implements MessageBody {

    private final BusinessMessage message;

    JsonMessage(BusinessMessage message) {
        this.message = message;
    }

    @Override
    public byte[] getBytes() {
        try {
            Generator.getJsonHolder().writeValueAsBytes(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
