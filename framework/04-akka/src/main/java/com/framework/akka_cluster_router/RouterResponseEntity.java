package com.framework.akka_cluster_router;

import com.framework.message.MessageBus;

/**
 * Created by @panyao on 2017/9/25.
 */
public class RouterResponseEntity {

    private final MessageBus message;

    public RouterResponseEntity(MessageBus message) {
        this.message = message;
    }

    public static RouterResponseEntity newMessage(MessageBus message) {
        return new RouterResponseEntity(message);
    }

    public MessageBus getMessage() {
        return message;
    }

}
