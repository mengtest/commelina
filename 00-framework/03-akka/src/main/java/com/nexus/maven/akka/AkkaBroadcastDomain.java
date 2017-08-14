package com.nexus.maven.akka;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * Created by @panyao on 2017/8/14.
 * <p>
 * 广播就是一组人获取到一样的信息
 */
public class AkkaBroadcastDomain {

    private List<Long> userIds = Lists.newArrayList();
    private Object message;

    private AkkaBroadcastDomain() {

    }

    public static AkkaBroadcastDomain newBroadcast(long userId, Object message) {
        AkkaBroadcastDomain broadcast = new AkkaBroadcastDomain();
        broadcast.userIds.add(userId);
        broadcast.message = message;
        return broadcast;
    }

    public static AkkaBroadcastDomain newBroadcast(Collection<Long> userIds, Object message) {
        AkkaBroadcastDomain broadcast = new AkkaBroadcastDomain();
        broadcast.userIds.addAll(userIds);
        broadcast.message = message;
        return broadcast;
    }

    public AkkaBroadcastDomain addUserId(long userId) {
        this.userIds.add(userId);
        return this;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public Object getMessage() {
        return message;
    }
}
