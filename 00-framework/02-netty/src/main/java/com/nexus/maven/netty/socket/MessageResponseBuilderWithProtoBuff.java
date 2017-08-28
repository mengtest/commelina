package com.nexus.maven.netty.socket;

import com.google.protobuf.ByteString;
import com.nexus.maven.core.message.MessageBus;
import com.nexus.maven.proto.BusinessMessage;
import com.nexus.maven.proto.BusinessProtocol;
import com.nexus.maven.proto.SYSTEM_CODE_CONSTANTS;
import com.nexus.maven.proto.SocketMessage;

/**
 * Created by @panyao on 2017/8/24.
 */
class MessageResponseBuilderWithProtoBuff implements MessageResponseBuilder {

    @Override
    public Object createPushMessage(int domain, MessageBus responseMessage) {
        return createMessageWithType(domain, responseMessage, SYSTEM_CODE_CONSTANTS.NOTIFY_CODE);
    }

    @Override
    public Object createResponseMessage(int domain, MessageBus responseMessage) {
        return createMessageWithType(domain, responseMessage,SYSTEM_CODE_CONSTANTS.RESONSE_CODE);
    }

    @Override
    public Object createErrorMessage(int errorCode) {
        return SocketMessage
                .newBuilder()
                .setCode(SYSTEM_CODE_CONSTANTS.forNumber(errorCode))
                .build();
    }

    private Object createMessageWithType(int domain, MessageBus messageBus, SYSTEM_CODE_CONSTANTS type) {
        byte[] bytes = messageBus.getBytes();
        if (bytes == null) {
            throw new RuntimeException("serialize failed.");
        }
        return SocketMessage.newBuilder()
                .setCode(type)
                .setDomain(domain)
                .setMsg(BusinessMessage.newBuilder()
                        .setOpCode(messageBus.getOpCode())
                        .setVersion(messageBus.getVersion())
                        .setBp(BusinessProtocol.forNumber(messageBus.getBp().ordinal()))
                        .setMsg(ByteString.copyFrom(bytes))
                ).build();
    }

}
