package com.nexus.maven.core.message;

import com.google.common.base.Preconditions;

/**
 * Created by @panyao on 2017/8/25.
 */
public class ResponseMessage {

    private final MessageBus message;

    protected ResponseMessage(MessageBus messageBus) {
        this.message = messageBus;
    }

    public static ResponseMessage newMessage(MessageBus messageBus) {
        Preconditions.checkNotNull(messageBus);
        return new ResponseMessage(messageBus);
    }

    public MessageBus getMessage() {
        return message;
    }

}
