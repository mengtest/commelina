package com.framework.niosocket.message;

import com.framework.core.MessageBody;
import com.google.common.base.Preconditions;
import com.google.protobuf.Internal;

import java.io.Serializable;

/**
 *
 * @author @panyao
 * @date 2017/8/15
 */
public class WorldMessage implements Serializable {

    private final Internal.EnumLite opcode;
    private final MessageBody message;

    private WorldMessage(Internal.EnumLite opcode, MessageBody messageBody) {
        this.opcode = opcode;
        this.message = messageBody;
    }

    public static WorldMessage newMessage(Internal.EnumLite opcode, MessageBody messageBody) {
        Preconditions.checkNotNull(messageBody);
        return new WorldMessage(opcode, messageBody);
    }

    public MessageBody getMessage() {
        return message;
    }

    public Internal.EnumLite getOpcode() {
        return opcode;
    }

}
