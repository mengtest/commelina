package com.framework.niosocket;

import com.framework.message.BroadcastMessage;
import com.framework.message.NotifyMessage;
import com.framework.message.WorldMessage;
import io.netty.channel.Channel;

/**
 * Created by @panyao on 2017/8/11.
 */

class MessageAdapter {

//    private static final Logger LOGGER = Logger.getLogger(MessageAdapter.class.getName());

    static void addNotify(int domain, NotifyMessage message) {
        Channel channel = NettyServerContext.getInstance().getUserChannel(message.getUserId());
        channel.writeAndFlush(MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE.createPushMessage(domain, message.getOpcode().getNumber(), message.getMessage()));
    }

    static void addBroadcast(int domain, BroadcastMessage message) {
        final Object msg = MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE.createPushMessage(domain, message.getOpcode().getNumber(), message.getMessage());

        for (long userId : message.getUserIds()) {
            Channel channel = NettyServerContext.getInstance().getUserChannel(userId);
            channel.writeAndFlush(msg);
        }
    }

    static void addWorld(int domain, WorldMessage message) {
        final Object msg = MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE.createPushMessage(domain, message.getOpcode().getNumber(), message.getMessage());

        for (Long userId : NettyServerContext.getInstance().LOGIN_USERS.values()) {
            Channel channel = NettyServerContext.getInstance().getUserChannel(userId);
            channel.writeAndFlush(msg);
        }
    }

}