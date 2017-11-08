package com.commelina.niosocket.message;

import com.commelina.core.MessageBody;

import java.util.List;


/**
 * 广播消息
 *
 * @author @panyao
 * @date 2017/8/15
 */
public class BroadcastMessage {

    private final int opcode;
    private final List<Long> userIds;
    private final MessageBody message;

    private BroadcastMessage(int opcode, List<Long> userIds, MessageBody messageBody) {
        this.opcode = opcode;
        this.message = messageBody;
        this.userIds = userIds;
    }

    public static BroadcastMessage newBroadcast(int opcode, List<Long> userIds, MessageBody messageBody) {
        return new BroadcastMessage(opcode, userIds, messageBody);
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public MessageBody getMessage() {
        return message;
    }

    public int getOpcode() {
        return opcode;
    }
}
