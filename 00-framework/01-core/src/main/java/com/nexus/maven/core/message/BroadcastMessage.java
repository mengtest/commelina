package com.nexus.maven.core.message;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by @panyao on 2017/8/15.
 * <p>
 * 广播消息
 */
public class BroadcastMessage {

    private final List<Long> userIds;
    private final MessageBus message;

    private BroadcastMessage(List<Long> userIds, MessageBus messageBus) {
        Preconditions.checkNotNull(userIds);
        this.message = messageBus;
        this.userIds = Lists.newArrayList(userIds);
    }

    public static BroadcastMessage newBroadcast(List<Long> userId, MessageBus messageBus) {
        Preconditions.checkNotNull(messageBus);
        return new BroadcastMessage(userId, messageBus);
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public MessageBus getMessage() {
        return message;
    }

}
