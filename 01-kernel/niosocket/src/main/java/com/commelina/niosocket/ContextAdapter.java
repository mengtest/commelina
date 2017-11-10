package com.commelina.niosocket;

import io.netty.channel.ChannelId;

/**
 * 用户会话上下文
 *
 * @author @panyao
 * @date 2017/8/25
 */
public class ContextAdapter {

    public static long userLogin(ChannelId channelId, long userId) {
        return NettyServerContext.INSTANCE.userJoin(channelId, userId);
    }

    public static ChannelId userLogout(long userId) {
        return NettyServerContext.INSTANCE.userRemove(userId);
    }

    public static long getLoginUserId(ChannelId channelId) {
        return NettyServerContext.INSTANCE.getLoginUserId(channelId);
    }

    public static boolean isOnline(long userId) {
        return NettyServerContext.INSTANCE.isOnline(userId);
    }

}
