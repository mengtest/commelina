package com.commelina.niosocket.message;

import com.google.common.base.Preconditions;
import com.google.protobuf.ByteString;

/**
 * 世界消息，广播消息给所有在线的用户
 *
 * @author @panyao
 * @date 2017/8/15
 */
public class WorldMessage {

    private final int opcode;
    private final ByteString body;

    private WorldMessage(int opcode, ByteString messageBody) {
        this.opcode = opcode;
        this.body = messageBody;
    }

    public static WorldMessage newMessage(int opcode, ByteString messageBody) {
        Preconditions.checkNotNull(messageBody);
        return new WorldMessage(opcode, messageBody);
    }

    public ByteString getBody() {
        return body;
    }

    public int getOpcode() {
        return opcode;
    }

}
