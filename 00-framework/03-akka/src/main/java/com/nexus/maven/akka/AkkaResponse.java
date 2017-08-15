package com.nexus.maven.akka;

import com.nexus.maven.core.message.ApiResponse;
import com.nexus.maven.core.message.MessageBus;

/**
 * Created by @panyao on 2017/8/14.
 */
public final class AkkaResponse implements ApiResponse {

    private final MessageBus messageBus;

    private AkkaResponse(MessageBus messageBus) {
        this.messageBus = messageBus;
    }

    public static AkkaResponse newReponse(MessageBus messageBus) {
        return new AkkaResponse(messageBus);
    }

    public MessageBus getMessage() {
        return messageBus;
    }
}
