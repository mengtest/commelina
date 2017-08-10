package com.nexus.maven.netty;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by @panyao on 2017/8/3.
 */
@Component
public final class NettyServerContext {

    // 用户会话组
    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup("sessionGroups", GlobalEventExecutor.INSTANCE);

    // 用户会话管理
    // FIXME: 2017/8/8 session 具体信息待商榷
    private final BiMap<ChannelId, Long> loginUsers = HashBiMap.create(128);

    private final Lock removeLock = new ReentrantLock();

    // 渠道上线
    public boolean channelActive(Channel channel) {
        return NettyServerContext.CHANNEL_GROUP.add(channel);
    }

    // 渠道下线
    public Long channelInactivee(Channel channel) {
        // 先把这个 channel 的用户都下线了
        Long userId = loginUsers.remove(channel.id());
        NettyServerContext.CHANNEL_GROUP.remove(channel);
        return userId;
    }

    // 把用户加入到登录会话中去
    public Long userJoin(ChannelId channelId, long userId) {
        return loginUsers.forcePut(channelId, userId);
    }

    // 用户注销了
    public ChannelId userRemove(long userId) {
        return loginUsers.inverse().remove(userId);
    }

    // 当前 channel 是否已经登录
    public Long channelLoginUserId(ChannelId channelId) {
        return loginUsers.get(channelId);
    }

    // 用户是否在线
    public boolean isOnline(long userId) {
        ChannelId channelId = loginUsers.inverse().get(userId);
        if (channelId == null) {
            return false;
        }
        Channel channel = CHANNEL_GROUP.find(channelId);
        if (channel == null || !channel.isActive()) {
            if (removeLock.tryLock()) {
                try {
                    loginUsers.inverse().remove(userId);
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
    public Channel getUserChannel(long userId) {
        ChannelId channelId = loginUsers.inverse().get(userId);
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
            this.channelInactivee(channel);
            throw new UserChannelUnActiveException();
        }
        return channel;
    }

}
