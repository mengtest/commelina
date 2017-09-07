package com.framework.niosocket;

import com.framework.message.MessageBus;
import com.framework.niosocket.proto.BusinessMessage;
import com.framework.niosocket.proto.BusinessProtocol;
import com.framework.niosocket.proto.SYSTEM_CODE_CONSTANTS;
import com.framework.niosocket.proto.SocketMessage;
import com.google.protobuf.ByteString;

/**
 * Created by @panyao on 2017/8/24.
 */
class MessageResponseBuilderWithProtoBuff implements MessageResponseBuilder {

    @Override
    public Object createPushMessage(int domain, int opcode, MessageBus messageBus) {
        return createMessageWithType(domain, opcode, messageBus, SYSTEM_CODE_CONSTANTS.NOTIFY_CODE);
    }

    @Override
    public Object createResponseMessage(int domain, int opcode, MessageBus messageBus) {
        return createMessageWithType(domain, opcode, messageBus, SYSTEM_CODE_CONSTANTS.RESONSE_CODE);
    }

    @Override
    public Object createErrorMessage(int systemErrorCode) {
        return SocketMessage
                .newBuilder()
                .setCode(SYSTEM_CODE_CONSTANTS.forNumber(systemErrorCode))
                .build();
    }

    private Object createMessageWithType(int domain, int opcode, MessageBus messageBus, SYSTEM_CODE_CONSTANTS type) {
        byte[] bytes = messageBus.getBytes();
        if (bytes == null) {
            return null;
        }
        return SocketMessage.newBuilder()
                .setCode(type)
                .setDomain(domain)
                .setOpcode(opcode)
                .setMsg(BusinessMessage.newBuilder()
                        .setBp(BusinessProtocol.forNumber(messageBus.getBp().ordinal()))
                        .setMsg(ByteString.copyFrom(bytes))
                        .setVersion(messageBus.getVersion())
                ).build();
    }

}
