package com.framework.niosocket;

import com.framework.message.MessageBus;
import com.framework.niosocket.proto.BusinessProtocol;
import com.framework.niosocket.proto.SERVER_CODE;
import com.framework.niosocket.proto.SocketMessage;
import com.google.protobuf.ByteString;

/**
 * Created by @panyao on 2017/8/24.
 */
class MessageResponseBuilderWithProtoBuff implements MessageResponseBuilder {

    @Override
    public Object createPushMessage(int domain, int opcode, MessageBus messageBus) {
        return createMessageWithType(domain, opcode, messageBus, SERVER_CODE.NOTIFY_CODE);
    }

    @Override
    public Object createResponseMessage(int domain, int opcode, MessageBus messageBus) {
        return createMessageWithType(domain, opcode, messageBus, SERVER_CODE.RESONSE_CODE);
    }

    @Override
    public Object createErrorMessage(SERVER_CODE serverCode) {
        return SocketMessage
                .newBuilder()
                .setCode(serverCode)
                .build();
    }

    private Object createMessageWithType(int domain, int opcode, MessageBus messageBus, SERVER_CODE type) {
        byte[] bytes = messageBus.getBytes();
        if (bytes == null) {
            return null;
        }
        return SocketMessage.newBuilder()
                .setCode(type)
                .setDomain(domain)
                .setOpcode(opcode)
                .setBp(BusinessProtocol.forNumber(messageBus.getBp().ordinal()))
                .setMsg(ByteString.copyFrom(bytes))
                .build();
    }

}
