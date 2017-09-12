package com.framework.message;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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

    public static MessageBus produceMessageForKV(final String k, final Object v) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(k));
        Preconditions.checkNotNull(v);
        Map<String, Object> kv = new HashMap<>();
        kv.put(k, v);

//        KVEntity entity = new KVEntity();
//        entity.k = k;
//        entity.v = v;
        return new JsonMessage(BusinessMessage.success(kv));
    }

    public static final class KVEntity implements Serializable {
        String k;
        Object v;

        public String getK() {
            return k;
        }

        public Object getV() {
            return v;
        }

    }

}