package com.framework.core;

import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/8/25.
 */
public class ResponseMessage {

    private final Internal.EnumLite opcode;
    private final MessageBus message;

    protected ResponseMessage(Internal.EnumLite opcode, MessageBus messageBus) {
        this.opcode = opcode;
        this.message = messageBus;
    }

    public static ResponseMessage newMessage(Internal.EnumLite opcode, MessageBus messageBus) {
        return new ResponseMessage(opcode, messageBus);
    }

    public MessageBus getMessage() {
        return message;
    }

    public Internal.EnumLite getOpcode() {
        return opcode;
    }
}
