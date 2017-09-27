package com.framework.niosocket;

import com.framework.message.MessageBus;
import com.framework.niosocket.proto.BusinessProtocol;
import com.framework.niosocket.proto.SERVER_CODE;
import com.framework.niosocket.proto.SocketMessage;
import com.google.protobuf.ByteString;
import com.google.protobuf.Internal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by @panyao on 2017/8/24.
 */
class MessageResponseBuilderWithProtoBuff implements MessageResponseBuilder {

    private final Logger logger = LoggerFactory.getLogger(MessageResponseBuilderWithProtoBuff.class);

    @Override
    public SocketMessage createPushMessage(Internal.EnumLite domain, Internal.EnumLite opcode, MessageBus messageBus) {
        return createMessageWithType(domain, opcode, messageBus, SERVER_CODE.NOTIFY_CODE);
    }

    @Override
    public SocketMessage createResponseMessage(Internal.EnumLite domain, Internal.EnumLite opcode, MessageBus messageBus) {
        return createMessageWithType(domain, opcode, messageBus, SERVER_CODE.RESONSE_CODE);
    }

    private SocketMessage createMessageWithType(Internal.EnumLite domain, Internal.EnumLite opcode, MessageBus messageBus, SERVER_CODE type) {
        byte[] bytes;
        try {
            bytes = messageBus.getBytes();
        } catch (IOException e) {
            logger.error("{}", e);
            return null;
        }
        return SocketMessage.newBuilder()
                .setCode(type)
                .setDomain(domain.getNumber())
                .setOpcode(opcode.getNumber())
                .setBp(BusinessProtocol.forNumber(messageBus.getBp().ordinal()))
                .setMsg(ByteString.copyFrom(bytes))
                .build();
    }

}
