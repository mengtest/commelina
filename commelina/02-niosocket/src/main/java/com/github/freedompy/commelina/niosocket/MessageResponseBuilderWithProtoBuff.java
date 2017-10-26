package com.github.freedompy.commelina.niosocket;

import com.github.freedompy.commelina.core.MessageBody;
import com.github.freedompy.commelina.niosocket.proto.SERVER_CODE;
import com.github.freedompy.commelina.niosocket.proto.SocketMessage;
import com.google.protobuf.ByteString;
import com.google.protobuf.Internal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * proto buff 的消息成器
 *
 * @author @panyao
 * @date 2017/8/24
 */
class MessageResponseBuilderWithProtoBuff implements MessageResponseBuilder {

    private final Logger logger = LoggerFactory.getLogger(MessageResponseBuilderWithProtoBuff.class);

    @Override
    public SocketMessage createPushMessage(Internal.EnumLite domain, int opcode, MessageBody messageBody) {
        return createMessageWithType(domain, opcode, messageBody, SERVER_CODE.NOTIFY_CODE);
    }

    @Override
    public SocketMessage createResponseMessage(Internal.EnumLite domain, int opcode, MessageBody messageBody) {
        return createMessageWithType(domain, opcode, messageBody, SERVER_CODE.RESONSE_CODE);
    }

    private SocketMessage createMessageWithType(Internal.EnumLite domain, int opcode, MessageBody body, SERVER_CODE type) {
        byte[] bytes;
        try {
            bytes = body.getBytes();
        } catch (IOException e) {
            logger.error("{}", e);
            return null;
        }
        return SocketMessage.newBuilder()
                .setCode(type)
                .setDomain(domain.getNumber())
                .setOpcode(opcode)
                .setMsg(ByteString.copyFrom(bytes))
                .build();
    }

}
