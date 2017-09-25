package com.framework.niosocket;

import com.framework.message.BroadcastMessage;
import com.framework.message.NotifyMessage;
import com.framework.message.WorldMessage;
import com.google.protobuf.Internal;
import io.netty.channel.Channel;

/**
 * Created by @panyao on 2017/8/11.
 */

public class MessageAdapter {
//    private static final Logger LOGGER = Logger.getLogger(MessageAdapter.class.getName());

    public static void addNotify(Internal.EnumLite domain, NotifyMessage message) {
        Channel channel = NettyServerContext.getInstance().getUserChannel(message.getUserId());
        channel.writeAndFlush(MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE.createPushMessage(domain, message.getOpcode(), message.getMessage()));
    }

    public static void addBroadcast(Internal.EnumLite domain, BroadcastMessage message) {
        final Object msg = MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE.createPushMessage(domain, message.getOpcode(), message.getMessage());

        for (Long userId : message.getUserIds()) {
            Channel channel = NettyServerContext.getInstance().getUserChannel(userId);
            channel.writeAndFlush(msg);
        }
    }

    public static void addWorld(Internal.EnumLite domain, WorldMessage message) {
        final Object msg = MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE.createPushMessage(domain, message.getOpcode(), message.getMessage());

        for (Long userId : NettyServerContext.getInstance().LOGIN_USERS.values()) {
            Channel channel = NettyServerContext.getInstance().getUserChannel(userId);
            channel.writeAndFlush(msg);
        }
    }

}