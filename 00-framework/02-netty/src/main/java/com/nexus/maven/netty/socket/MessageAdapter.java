package com.nexus.maven.netty.socket;

import com.nexus.maven.core.message.BroadcastMessage;
import com.nexus.maven.core.message.NotifyMessage;
import com.nexus.maven.core.message.WorldMessage;
import io.netty.channel.Channel;

import java.io.IOException;

/**
 * Created by @panyao on 2017/8/11.
 */

class MessageAdapter {

//    private static final Logger LOGGER = Logger.getLogger(MessageAdapter.class.getName());

    static void addNotify(int domain, NotifyMessage notifyResponse) throws IOException {
        Channel channel = NettyServerContext.getInstance().getUserChannel(notifyResponse.getUserId());
        channel.writeAndFlush(MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE.createPushMessage(domain, notifyResponse.getMessage()));
    }

    static void addBroadcast(int domain, BroadcastMessage broadcastMessage) throws IOException {
        final Object msg = MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE.createPushMessage(domain, broadcastMessage.getMessage());

        for (long userId : broadcastMessage.getUserIds()) {
            Channel channel = NettyServerContext.getInstance().getUserChannel(userId);
            channel.writeAndFlush(msg);
        }
    }

    static void addWorld(int domain, WorldMessage worldMessage) throws IOException {
        final Object msg = MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE.createPushMessage(domain, worldMessage.getMessage());

        for (Long userId : NettyServerContext.getInstance().LOGIN_USERS.values()) {
            Channel channel = NettyServerContext.getInstance().getUserChannel(userId);
            channel.writeAndFlush(msg);
        }
    }

}