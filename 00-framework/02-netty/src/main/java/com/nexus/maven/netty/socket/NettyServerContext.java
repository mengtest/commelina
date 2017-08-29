package com.nexus.maven.netty.socket;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class NettyServerContext {

    // 用户会话组
    private final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup("sessionGroups", GlobalEventExecutor.INSTANCE);
    // 用户会话管理
    final BiMap<ChannelId, Long> LOGIN_USERS = HashBiMap.create(128);

    private final Lock removeLock = new ReentrantLock();

    private NettyServerContext() {

    }

    // 用户上线
    boolean channelActive(Channel channel) {
        return CHANNEL_GROUP.add(channel);
    }

    // 用户下线
    Long channelInactive(Channel channel) {
        // 先把这个 channel 的用户都下线了
        Long userId = LOGIN_USERS.remove(channel.id());
        CHANNEL_GROUP.remove(channel);
        return userId;
    }

    // 把用户加入到登录会话中去
    Long userJoin(ChannelId channelId, long userId) {
        Channel channel = CHANNEL_GROUP.find(channelId);
        if (channel == null) {
            throw new UserChannelUnActiveException();
        }
        return LOGIN_USERS.forcePut(channelId, userId);
    }

    // 用户注销了
    ChannelId userRemove(long userId) {
        ChannelId channelId = LOGIN_USERS.inverse().remove(userId);
        if (channelId != null) {
            // FIXME: 2017/8/29 bian yi jian cha
            CHANNEL_GROUP.remove(CHANNEL_GROUP.find(channelId));
        }
//        CHANNEL_GROUP.remove(channelId);
        return channelId;
    }

    long userRemove(ChannelId channelId) {
        long userId = LOGIN_USERS.remove(channelId);
        // FIXME: 2017/8/29 bian yi jian cha
        CHANNEL_GROUP.remove(CHANNEL_GROUP.find(channelId));
        return userId;
    }

    long getLoginUserId(ChannelId channelId) {
        if (CHANNEL_GROUP.find(channelId) == null) {
            return 0l;
        }
        Long userId = LOGIN_USERS.get(channelId);
        if (userId == null || userId <= 0) {
            return 0l;
        }
        return userId;
    }

    ChannelId getLoginChannelId(long userId) {
        ChannelId channelId = LOGIN_USERS.inverse().get(userId);
        if (channelId == null) {
            return null;
        }
        if (CHANNEL_GROUP.find(channelId) != null) {
            return channelId;
        }
        return null;
    }

    // 用户是否在线
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

    private static final NettyServerContext nettyServerContext = new NettyServerContext();

    static NettyServerContext getInstance() {
        return nettyServerContext;
    }

}