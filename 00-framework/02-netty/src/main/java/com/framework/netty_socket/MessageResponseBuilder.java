package com.framework.netty_socket;

import com.framework.core_message.MessageBus;

/**
 * Created by @panyao on 2017/8/24.
 */
interface MessageResponseBuilder {

    Object createPushMessage(int domain, int opcode, MessageBus messageBus);

    Object createResponseMessage(int domain, int opcode, MessageBus messageBus);

    Object createErrorMessage(int systemErrorCode);

}
