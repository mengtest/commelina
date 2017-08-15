package com.nexus.maven.core.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.nexus.maven.utils.Generator;

/**
 * Created by @panyao on 2017/8/10.
 */
public final class JsonMessage implements MessageBus {

    private static final BusinessMessage EMPTY_RESPONSE_JSON_MESSAGE =
            BusinessMessage.success();

    private final int opCode;
    private final BusinessMessage message;

    private JsonMessage(int opCode, BusinessMessage message) {
        this.opCode = opCode;
        this.message = message;
    }

    public static JsonMessage newHandler(int opCode) {
        Preconditions.checkArgument(opCode >= 0);
        return new JsonMessage(opCode, EMPTY_RESPONSE_JSON_MESSAGE);
    }

    public static JsonMessage newHandler(int opCode, BusinessMessage message) {
        Preconditions.checkArgument(opCode >= 0);
        Preconditions.checkNotNull(message);
        return new JsonMessage(opCode, message);
    }

    public static JsonMessage newHandlerJSONKV(int opCode, String k, Object v) {
        Preconditions.checkArgument(opCode >= 0);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(k));
        Preconditions.checkNotNull(v);
        KVEntity entity = new KVEntity();
        entity.k = k;
        entity.v = v;
        return new JsonMessage(opCode, BusinessMessage.success(entity));
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

    private static class KVEntity {
        String k;
        Object v;
    }

}
