package org.commelina.core;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * json 消息生成者,可以不修改对外接口更换实现
 *
 * @author @panyao
 * @date 2017/8/16
 */
public class JsonMessageProvider {

    private static final MessageBody EMPTY = new JsonMessage(BusinessMessage.success());

    public static MessageBody produceMessage() {
        return EMPTY;
    }

    public static MessageBody produceMessage(BusinessMessage message) {
        Preconditions.checkNotNull(message);
        return new JsonMessage(message);
    }

    public static MessageBody produceMessageForKV(final String k, final Object v) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(k));
        Preconditions.checkNotNull(v);
        final Map<String, Object> kv = Maps.newHashMap();
        kv.put(k, v);
        return new JsonMessage(BusinessMessage.success(kv));
    }

}