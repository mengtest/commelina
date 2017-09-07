package com.framework.niosocket;

import com.framework.message.MessageBus;
import com.framework.niosocket.proto.SERVER_CODE;

/**
 * Created by @panyao on 2017/8/24.
 */
interface MessageResponseBuilder {

    Object createPushMessage(int domain, int opcode, MessageBus messageBus);

    Object createResponseMessage(int domain, int opcode, MessageBus messageBus);

    Object createErrorMessage(SERVER_CODE serverCode);

}
