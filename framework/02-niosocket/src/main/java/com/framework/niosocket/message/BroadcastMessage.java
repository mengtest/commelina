package com.framework.niosocket.message;

import com.framework.core.MessageBody;
import com.google.protobuf.Internal;

import java.io.Serializable;


/**
 * 广播消息
 *
 * @author @panyao
 * @date 2017/8/15
 */
public class BroadcastMessage implements Serializable {

    private final Internal.EnumLite opcode;
    private final long[] userIds;
    private final MessageBody message;

    private BroadcastMessage(Internal.EnumLite opcode, long[] userIds, MessageBody messageBody) {
        this.opcode = opcode;
        this.message = messageBody;
        this.userIds = userIds;
    }

    public static BroadcastMessage newBroadcast(Internal.EnumLite opcode, long[] userIds, MessageBody messageBody) {
        return new BroadcastMessage(opcode, userIds, messageBody);
    }

    public long[] getUserIds() {
        return userIds;
    }

    public MessageBody getMessage() {
        return message;
    }

    public Internal.EnumLite getOpcode() {
        return opcode;
    }
}
