package com.nexus.maven.akka;

import com.nexus.maven.core.message.MessageBus;

/**
 * Created by @panyao on 2017/8/14.
 */
public final class AkkaResponse {

    private MessageBus messageBus;

    public static AkkaResponse newResponse(MessageBus messageBus) {
        AkkaResponse response = new AkkaResponse();
        response.setMessageBus(messageBus);
        return response;
    }

    public MessageBus getMessage() {
        return messageBus;
    }

    public void setMessageBus(MessageBus messageBus) {
        this.messageBus = messageBus;
    }

}
