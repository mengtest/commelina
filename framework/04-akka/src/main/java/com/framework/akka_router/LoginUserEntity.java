package com.framework.akka_router;

import com.framework.message.MessageBus;

/**
 * Created by @panyao on 2017/9/30.
 */
public class LoginUserEntity {

    private final long userId;
    private final MessageBus messageBus;

    public LoginUserEntity(long userId, MessageBus messageBus) {
        this.userId = userId;
        this.messageBus = messageBus;
    }

    public long getUserId() {
        return userId;
    }

    public MessageBus getMessageBus() {
        return messageBus;
    }
}
