package com.nexus.maven.netty.socket;

import com.nexus.maven.core.message.BroadcastMessage;
import com.nexus.maven.core.message.NotifyMessage;
import com.nexus.maven.core.message.WorldMessage;
import io.netty.channel.Channel;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Created by @panyao on 2017/8/11.
 */

public class MessageAdapter {

    private static final Logger LOGGER = Logger.getLogger(MessageAdapter.class.getName());

    public static void addNotify(int domain, NotifyMessage notifyResponse) throws IOException {
        Channel channel = NettyServerContext.getInstance().getUserChannel(notifyResponse.getUserId());
        channel.writeAndFlush(MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE.createPushMessage(domain, notifyResponse.getMessage()));
    }

    public static void addBroadcast(int domain, BroadcastMessage broadcastMessage) throws IOException {
        final Object msg = MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE.createPushMessage(domain, broadcastMessage.getMessage());
        Iterator<Long> iterator = broadcastMessage.getUserIds().iterator();
        while (iterator.hasNext()) {
            Channel channel = NettyServerContext.getInstance().getUserChannel(iterator.next());
            channel.writeAndFlush(msg);
        }
    }

    public static void addWorld(int domain, WorldMessage worldMessage) throws IOException {
        final Object msg = MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE.createPushMessage(domain, worldMessage.getMessage());
        Iterator<Long> iterator = NettyServerContext.getInstance().LOGIN_USERS.values().iterator();
        while (iterator.hasNext()) {
            Channel channel = NettyServerContext.getInstance().getUserChannel(iterator.next());
            channel.writeAndFlush(msg);
        }
    }

}