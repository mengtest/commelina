package com.framework.niosocket;

import io.netty.channel.ChannelId;

/**
 * Created by @panyao on 2017/8/25.
 */
public class ContextAdapter {

//    private static final Logger LOGGER = Logger.getLogger(ContextAdapter.class.getName());

    public static long userLogin(ChannelId channelId, long userId) {
        return NettyServerContext.getInstance().userJoin(channelId, userId);
    }

    public static long userLogout(ChannelId channelId) {
        return NettyServerContext.getInstance().userRemove(channelId);
    }

    public static ChannelId userLogout(long userId) {
        return NettyServerContext.getInstance().userRemove(userId);
    }

    public static long getLoginUserId(ChannelId channelId) {
        return NettyServerContext.getInstance().getLoginUserId(channelId);
    }

    public static ChannelId getLoginChannelId(long userId) {
        return NettyServerContext.getInstance().getLoginChannelId(userId);
    }

    public static boolean isOnline(long userId) {
        return NettyServerContext.getInstance().isOnline(userId);
    }

}
