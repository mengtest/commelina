package com.framework.niosocket.message;

import com.framework.core.MessageBody;

/**
 * @author @panyao
 * @date 2017/8/15
 */
public class NotifyMessage {

    private final int opcode;
    private final long userId;
    private final MessageBody message;

    private NotifyMessage(int opcode, long userId, MessageBody messageBody) {
        this.opcode = opcode;
        this.userId = userId;
        this.message = messageBody;
    }

    public static NotifyMessage newMessage(int opcode, long userId, MessageBody messageBody) {
        return new NotifyMessage(opcode, userId, messageBody);
    }

    public long getUserId() {
        return userId;
    }

    public MessageBody getMessage() {
        return message;
    }

    public int getOpcode() {
        return opcode;
    }
}
