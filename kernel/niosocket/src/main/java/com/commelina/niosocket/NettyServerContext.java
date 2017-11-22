package com.commelina.niosocket;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * socket server 上下文
 *
 * @author @panyao
 * @date 2017/8/11
 */
class NettyServerContext {

    /**
     * 用户会话组
     */
    final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup("sessionGroups", GlobalEventExecutor.INSTANCE);
    /**
     * 用户会话管理
     */
    final BiMap<ChannelId, Long> LOGIN_USERS = HashBiMap.create(128);

    private final Lock removeLock = new ReentrantLock();

    static final NettyServerContext INSTANCE = new NettyServerContext();

    private NettyServerContext() {

    }

    /**
     * 用户下线
     *
     * @param channel
     * @return
     */
    long channelInactive(Channel channel) {
        // 先把这个 channel 的用户都下线了
        Long userId = LOGIN_USERS.remove(channel.id());
        CHANNEL_GROUP.remove(channel);
        return userId == null ? 0 : userId;
    }

    /**
     * 把用户加入到登录会话中去
     */
    void userJoin(Channel channel, long userId) {
        CHANNEL_GROUP.add(channel);
        LOGIN_USERS.forcePut(channel.id(), userId);
    }

    /**
     * 根据channel id 获取用户登录的 user id
     *
     * @param channelId
     * @return
     */
    Long getLoginUserId(ChannelId channelId) {
        if (CHANNEL_GROUP.find(channelId) == null) {
            return null;
        }
        return LOGIN_USERS.get(channelId);
    }

    /**
     * 用户是否在线
     *
     * @param userId
     * @return
     */
    boolean isOnline(long userId) {
        ChannelId channelId = LOGIN_USERS.inverse().get(userId);
        if (channelId == null) {
            return false;
        }
        Channel channel = CHANNEL_GROUP.find(channelId);
        if (channel == null || !channel.isActive()) {
            if (removeLock.tryLock()) {
                try {
                    LOGIN_USERS.inverse().remove(userId);
                } finally {
                    removeLock.unlock();
                }
            }
            return false;
        }
        return true;
    }

    /**
     * 获取用户的 channel
     *
     * @param userId
     * @return
     */
    Channel getUserChannel(long userId) {
        ChannelId channelId = LOGIN_USERS.inverse().get(userId);
        if (channelId == null) {
            // 用户未登录
            throw new UserUnLoginException();
        }
        Channel channel = CHANNEL_GROUP.find(channelId);
        if (channel == null) {
            // 用户下线了
            throw new UserChannelNotFoundException();
        }
        if (!channel.isActive()) {
            this.channelInactive(channel);
            throw new UserChannelUnActiveException();
        }
        return channel;
    }

}