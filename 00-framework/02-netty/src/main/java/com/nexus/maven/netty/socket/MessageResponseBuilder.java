package com.nexus.maven.netty.socket;

import com.nexus.maven.core.message.MessageBus;

/**
 * Created by @panyao on 2017/8/24.
 */
interface MessageResponseBuilder {

    Object createPushMessage(int domain, MessageBus responseMessage);

    Object createResponseMessage(int domain, MessageBus responseMessage);

    Object createErrorMessage(int errorCode);

}
