package com.framework.niosocket;

import com.framework.core.MessageBody;
import com.framework.niosocket.proto.SocketMessage;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/8/24.
 */
public interface MessageResponseBuilder {

    SocketMessage createPushMessage(Internal.EnumLite domain, int opcode, MessageBody messageBody);

    SocketMessage createResponseMessage(Internal.EnumLite domain, int opcode, MessageBody messageBody);

}