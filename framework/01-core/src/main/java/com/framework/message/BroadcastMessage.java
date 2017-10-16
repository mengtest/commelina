package com.framework.message;

import com.google.protobuf.Internal;

import java.io.Serializable;

/**
 * Created by @panyao on 2017/8/15.
 * <p>
 * 广播消息
 */
public class BroadcastMessage implements Serializable {

    private final Internal.EnumLite opcode;
    private final long[] userIds;
    private final MessageBus message;

    private BroadcastMessage(Internal.EnumLite opcode, long[] userIds, MessageBus messageBus) {
        this.opcode = opcode;
        this.message = messageBus;
        this.userIds = userIds;
    }

    public static BroadcastMessage newBroadcast(Internal.EnumLite opcode, long[] userIds, MessageBus messageBus) {
        return new BroadcastMessage(opcode, userIds, messageBus);
    }

    public long[] getUserIds() {
        return userIds;
    }

    public MessageBus getMessage() {
        return message;
    }

    public Internal.EnumLite getOpcode() {
        return opcode;
    }
}
