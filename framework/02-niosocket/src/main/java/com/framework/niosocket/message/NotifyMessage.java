package com.framework.niosocket.message;

import com.framework.core.MessageBody;
import com.google.protobuf.Internal;

import java.io.Serializable;

/**
 * Created by @panyao on 2017/8/15.
 */
public class NotifyMessage implements Serializable {

    private final Internal.EnumLite opcode;
    private final long userId;
    private final MessageBody message;

    private NotifyMessage(Internal.EnumLite opcode, long userId, MessageBody messageBody) {
        this.opcode = opcode;
        this.userId = userId;
        this.message = messageBody;
    }

    public static NotifyMessage newMessage(Internal.EnumLite opcode, long userId, MessageBody messageBody) {
        return new NotifyMessage(opcode, userId, messageBody);
    }

    public long getUserId() {
        return userId;
    }

    public MessageBody getMessage() {
        return message;
    }

    public Internal.EnumLite getOpcode() {
        return opcode;
    }
}
