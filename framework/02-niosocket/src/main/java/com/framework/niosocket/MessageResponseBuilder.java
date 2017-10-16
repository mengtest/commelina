package com.framework.niosocket;

import com.framework.core.MessageBus;
import com.framework.niosocket.proto.SocketMessage;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/8/24.
 */
public interface MessageResponseBuilder {

    SocketMessage createPushMessage(Internal.EnumLite domain, int opcode, MessageBus messageBus);

    SocketMessage createResponseMessage(Internal.EnumLite domain, int opcode, MessageBus messageBus);

}
