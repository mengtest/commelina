package com.nexus.maven.netty.socket;

import io.netty.channel.ChannelId;

import java.util.logging.Logger;

/**
 * Created by @panyao on 2017/8/25.
 */
public class ContextAdapter {

    private static final Logger LOGGER = Logger.getLogger(ContextAdapter.class.getName());

    public static long userJoin(ChannelId channelId, long userId) {
        return NettyServerContext.getInstance().userJoin(channelId, userId);
    }

}
