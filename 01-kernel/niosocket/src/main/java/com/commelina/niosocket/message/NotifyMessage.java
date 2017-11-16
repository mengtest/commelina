package com.commelina.niosocket.message;

import com.google.protobuf.ByteString;

/**
 * 通知消息
 *
 * @author @panyao
 * @date 2017/8/15
 */
public class NotifyMessage {

    private final int opcode;
    private final long userId;
    private final ByteString body;

    private NotifyMessage(int opcode, long userId, ByteString messageBody) {
        this.opcode = opcode;
        this.userId = userId;
        this.body = messageBody;
    }

    public static NotifyMessage newMessage(int opcode, long userId, ByteString messageBody) {
        return new NotifyMessage(opcode, userId, messageBody);
    }

    public long getUserId() {
        return userId;
    }

    public ByteString getBody() {
        return body;
    }

    public int getOpcode() {
        return opcode;
    }
}
