package com.commelina.niosocket.message;

import com.google.protobuf.ByteString;

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
    private final ByteString body;

    private BroadcastMessage(int opcode, List<Long> userIds, ByteString messageBody) {
        this.opcode = opcode;
        this.body = messageBody;
        this.userIds = userIds;
    }

    public static BroadcastMessage newBroadcast(int opcode, List<Long> userIds, ByteString messageBody) {
        return new BroadcastMessage(opcode, userIds, messageBody);
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public ByteString getBody() {
        return body;
    }

    public int getOpcode() {
        return opcode;
    }
}
