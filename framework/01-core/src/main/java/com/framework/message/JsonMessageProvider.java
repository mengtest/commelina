package com.framework.message;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Created by @panyao on 2017/8/16.
 */
public class JsonMessageProvider {

    private static final BusinessMessage EMPTY_RESPONSE_MESSAGE =
            BusinessMessage.success();

    public static MessageBus produceMessage() {
        return new JsonMessage(EMPTY_RESPONSE_MESSAGE);
    }

    public static MessageBus produceMessage(BusinessMessage message) {
        Preconditions.checkNotNull(message);
        return new JsonMessage(message);
    }


    public static MessageBus produceMessageForKV(String k, Object v) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(k));
        Preconditions.checkNotNull(v);
        KVEntity entity = new KVEntity();
        entity.k = k;
        entity.v = v;
        return new JsonMessage(BusinessMessage.success(entity));
    }

    public static final class KVEntity {
        String k;
        Object v;
    }

}
