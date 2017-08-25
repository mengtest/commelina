package com.nexus.maven.core.message;

import com.google.common.base.Preconditions;

/**
 * Created by @panyao on 2017/8/15.
 */
public class NotifyMessage {

    private final long userId;
    private final MessageBus message;

    private NotifyMessage(long userId, MessageBus messageBus) {
        Preconditions.checkArgument(userId > 0);
        this.userId = userId;
        this.message = messageBus;
    }

    public static NotifyMessage newMessage(long userId, MessageBus messageBus) {
        Preconditions.checkNotNull(messageBus);
        return new NotifyMessage(userId, messageBus);
    }

    public long getUserId() {
        return userId;
    }

    public MessageBus getMessage() {
        return message;
    }

}
