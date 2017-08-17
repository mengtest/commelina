package com.nexus.maven.akka;

import com.nexus.maven.core.message.MessageBus;
import com.nexus.maven.core.message.NotifyResponse;

/**
 * Created by @panyao on 2017/8/15.
 */
public class AkkaNotify implements NotifyResponse {

    private long userId;
    private MessageBus messageBus;

    public static AkkaNotify newNotify(long userId, MessageBus messageBus) {
        AkkaNotify notify = new AkkaNotify();
        notify.setUserId(userId);
        notify.setMessageBus(messageBus);
        return notify;
    }

    @Override
    public MessageBus getMessage() {
        return messageBus;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setMessageBus(MessageBus messageBus) {
        this.messageBus = messageBus;
    }
}
