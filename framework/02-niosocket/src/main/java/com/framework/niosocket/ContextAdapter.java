package com.framework.niosocket;

import io.netty.channel.ChannelId;

/**
 * Created by @panyao on 2017/8/25.
 */
public class ContextAdapter {

    public static long userLogin(ChannelId channelId, long userId) {
        return NettyServerContext.Holder.INSTANCE.userJoin(channelId, userId);
    }

    public static long userLogout(ChannelId channelId) {
        return NettyServerContext.Holder.INSTANCE.userRemove(channelId);
    }

    public static ChannelId userLogout(long userId) {
        return NettyServerContext.Holder.INSTANCE.userRemove(userId);
    }

    public static long getLoginUserId(ChannelId channelId) {
        return NettyServerContext.Holder.INSTANCE.getLoginUserId(channelId);
    }

    public static ChannelId getLoginChannelId(long userId) {
        return NettyServerContext.Holder.INSTANCE.getLoginChannelId(userId);
    }

    public static boolean isOnline(long userId) {
        return NettyServerContext.Holder.INSTANCE.isOnline(userId);
    }

}
