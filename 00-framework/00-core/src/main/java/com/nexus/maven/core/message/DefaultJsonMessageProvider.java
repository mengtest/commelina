package com.nexus.maven.core.message;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Created by @panyao on 2017/8/16.
 */
public class DefaultJsonMessageProvider {

    private static final BusinessMessage EMPTY_RESPONSE_JSON_MESSAGE =
            BusinessMessage.success();

    public static MessageBus newMessage(int opCode) {
        Preconditions.checkArgument(opCode >= 0);
        return new JsonMessage(opCode, EMPTY_RESPONSE_JSON_MESSAGE);
    }

    public static MessageBus newMessage(int opCode, BusinessMessage message) {
        Preconditions.checkArgument(opCode >= 0);
        Preconditions.checkNotNull(message);
        return new JsonMessage(opCode, message);
    }

    public static MessageBus newMessageForKV(int opCode, String k, Object v) {
        Preconditions.checkArgument(opCode >= 0);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(k));
        Preconditions.checkNotNull(v);
        KVEntity entity = new KVEntity();
        entity.k = k;
        entity.v = v;
        return new JsonMessage(opCode, BusinessMessage.success(entity));
    }

    private static class KVEntity {
        String k;
        Object v;
    }
}
