package com.nexus.maven.core.message;

import com.google.common.base.Preconditions;

/**
 * Created by @panyao on 2017/8/15.
 * <p>
 * 广播消息
 */
public class BroadcastMessage {

    private final long[] userIds;
    private final MessageBus message;

    private BroadcastMessage(long[] userIds, MessageBus messageBus) {
        Preconditions.checkNotNull(userIds);
        this.message = messageBus;
        this.userIds = userIds;
    }

    public static BroadcastMessage newBroadcast(long[] userIds, MessageBus messageBus) {
        Preconditions.checkArgument(userIds != null && userIds.length > 0);
        Preconditions.checkNotNull(messageBus);
        return new BroadcastMessage(userIds, messageBus);
    }

    public long[] getUserIds() {
        return userIds;
    }

    public MessageBus getMessage() {
        return message;
    }

}
