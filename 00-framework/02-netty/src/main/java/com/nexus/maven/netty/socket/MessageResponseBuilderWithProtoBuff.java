package com.nexus.maven.netty.socket;

import com.google.protobuf.ByteString;
import com.nexus.maven.core.message.MessageBus;
import io.socket.netty.proto.SocketNettyProtocol;

/**
 * Created by @panyao on 2017/8/24.
 */
class MessageResponseBuilderWithProtoBuff implements MessageResponseBuilder {

    @Override
    public Object createPushMessage(int domain, MessageBus responseMessage) {
        return createMessageWithType(domain, responseMessage, SocketNettyProtocol.SYSTEM_CODE_CONSTANTS.NOTIFY_CODE);
    }

    @Override
    public Object createResponseMessage(int domain, MessageBus responseMessage) {
        return createMessageWithType(domain, responseMessage, SocketNettyProtocol.SYSTEM_CODE_CONSTANTS.RESONSE_CODE);
    }

    @Override
    public Object createErrorMessage(int errorCode) {
        return SocketNettyProtocol.SocketMessage
                .newBuilder()
                .setCode(SocketNettyProtocol.SYSTEM_CODE_CONSTANTS.forNumber(errorCode))
                .build();
    }

    private Object createMessageWithType(int domain, MessageBus messageBus, SocketNettyProtocol.SYSTEM_CODE_CONSTANTS type) {
        byte[] bytes = messageBus.getBytes();
        if (bytes == null) {
            throw new RuntimeException("serialize failed.");
        }
        return SocketNettyProtocol.SocketMessage.newBuilder()
                .setCode(type)
                .setDomain(domain)
                .setMsg(SocketNettyProtocol.BusinessMessage.newBuilder()
                        .setOpCode(messageBus.getOpCode())
                        .setVersion(messageBus.getVersion())
                        .setBp(SocketNettyProtocol.BusinessProtocol.forNumber(messageBus.getBp().ordinal()))
                        .setMsg(ByteString.copyFrom(bytes))
                ).build();
    }

}
