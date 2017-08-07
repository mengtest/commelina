package com.game.framework.netty;

import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by @panyao on 2017/8/3.
 */
@Component
public final class NettyServerContext {

    // 默认的用户组管理
    public static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup("ChannelGroups", GlobalEventExecutor.INSTANCE);

    private Map<Long, ChannelId> loginUsers = Maps.newConcurrentMap();

    // 把用户加入到登录会话中去
    public void userJoin(long userId, ChannelId channelId) {
        loginUsers.put(userId, channelId);
    }

    // 用户注销了
    public void userRemove(long userId) {
        loginUsers.remove(userId);
    }

    // 用户是否在线
    public boolean isOnline(long userId) {
        ChannelId channelId = loginUsers.get(userId);
        if (channelId == null) {
            return false;
        }
        Channel channel = CHANNEL_GROUP.find(channelId);
        if (channel == null) {
            loginUsers.remove(userId);
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
        ChannelId channelId = loginUsers.get(userId);
        if (channelId == null) {
            // 用户未登录
            throw new UserUnLoginException();
        }
        Channel channel = CHANNEL_GROUP.find(channelId);
        if (channel == null) {
            // 用户下线了
            throw new UserChannelNotFoundException();
        }
        return channel;
    }

}
