package com.nexus.maven.core.message;

import com.google.common.base.Preconditions;

/**
 * Created by @panyao on 2017/8/15.
 */
public class WorldMessage {

    private final MessageBus message;

    private WorldMessage(MessageBus messageBus) {
        this.message = messageBus;
    }

    public static WorldMessage newMessage(MessageBus messageBus) {
        Preconditions.checkNotNull(messageBus);
        return new WorldMessage(messageBus);
    }

    public MessageBus getMessage() {
        return message;
    }

}
