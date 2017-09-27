package com.framework.niosocket;

import com.framework.message.MessageBus;
import com.framework.niosocket.proto.SERVER_CODE;
import com.framework.niosocket.proto.SocketMessage;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/8/24.
 */
public interface MessageResponseBuilder {

    SocketMessage createPushMessage(Internal.EnumLite domain, Internal.EnumLite opcode, MessageBus messageBus);

    SocketMessage createResponseMessage(Internal.EnumLite domain, Internal.EnumLite opcode, MessageBus messageBus);

}
