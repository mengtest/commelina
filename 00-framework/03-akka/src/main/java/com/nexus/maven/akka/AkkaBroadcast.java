package com.nexus.maven.akka;

import com.google.common.collect.Lists;
import com.nexus.maven.core.message.BroadcastResponse;
import com.nexus.maven.core.message.MessageBus;

import java.util.List;

/**
 * Created by @panyao on 2017/8/14.
 * <p>
 * 广播就是一组人获取到一样的信息
 */
public class AkkaBroadcast implements BroadcastResponse {

    private final List<Long> userIds = Lists.newArrayList();
    private final MessageBus message;

    private AkkaBroadcast(MessageBus message) {
        this.message = message;
    }

    public static AkkaBroadcast newBroadcast(long userId, MessageBus bus) {
        AkkaBroadcast broadcast = new AkkaBroadcast(bus);
        broadcast.userIds.add(userId);
        return broadcast;
    }

    public static AkkaBroadcast newBroadcast(long[] userIds, MessageBus bus) {
        AkkaBroadcast broadcast = new AkkaBroadcast(bus);
        for (long userId : userIds) {
            broadcast.userIds.add(userId);
        }
        return broadcast;
    }

    public AkkaBroadcast addUserId(long userId) {
        this.userIds.add(userId);
        return this;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public MessageBus getMessage() {
        return message;
    }
}
