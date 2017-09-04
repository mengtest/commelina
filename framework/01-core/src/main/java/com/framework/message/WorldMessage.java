package com.framework.message;

import com.google.common.base.Preconditions;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/8/15.
 */
public class WorldMessage {

    private final Internal.EnumLite opcode;
    private final MessageBus message;

    private WorldMessage(Internal.EnumLite opcode, MessageBus messageBus) {
        this.opcode = opcode;
        this.message = messageBus;
    }

    public static WorldMessage newMessage(Internal.EnumLite opcode,MessageBus messageBus) {
        Preconditions.checkNotNull(messageBus);
        return new WorldMessage(opcode, messageBus);
    }

    public MessageBus getMessage() {
        return message;
    }

    public Internal.EnumLite getOpcode() {
        return opcode;
    }

}
