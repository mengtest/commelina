package com.nexus.maven.akka;

import com.nexus.maven.core.message.MessageBus;
import com.nexus.maven.core.message.NotifyResponse;

/**
 * Created by @panyao on 2017/8/15.
 */
public class AkkaNotify implements NotifyResponse {

    private final long userId;
    private final MessageBus messageBus;

    private AkkaNotify(long userId, MessageBus messageBus) {
        this.userId = userId;
        this.messageBus = messageBus;
    }

    public static AkkaNotify newNotify(long userId, MessageBus messageBus) {
        return new AkkaNotify(userId, messageBus);
    }

    @Override
    public MessageBus getMessage() {
        return messageBus;
    }

    @Override
    public long getUserId() {
        return userId;
    }

}
