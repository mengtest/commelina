package com.framework.core;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Created by @panyao on 2017/8/16.
 */
public class JsonMessageProvider {

    private static final BusinessMessage EMPTY_RESPONSE_MESSAGE =
            BusinessMessage.success();

    public static MessageBus produceMessage() {
        return new JsonMessage(EMPTY_RESPONSE_MESSAGE, null);
    }

    public static MessageBus produceMessage(BusinessMessage message) {
        Preconditions.checkNotNull(message);
        return new JsonMessage(message, null);
    }

    public static MessageBus produceMessage(BusinessMessage message, String version) {
        Preconditions.checkNotNull(message);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(version));
        return new JsonMessage(message, version);
    }

    public static MessageBus produceMessageForKV(String k, Object v) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(k));
        Preconditions.checkNotNull(v);
        KVEntity entity = new KVEntity();
        entity.k = k;
        entity.v = v;
        return new JsonMessage(BusinessMessage.success(entity), null);
    }

    public static MessageBus produceMessageForKV(String k, Object v, String version) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(k));
        Preconditions.checkNotNull(v);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(version));
        KVEntity entity = new KVEntity();
        entity.k = k;
        entity.v = v;
        return new JsonMessage(BusinessMessage.success(entity), version);
    }

    public static final class KVEntity {
        String k;
        Object v;
    }

}
