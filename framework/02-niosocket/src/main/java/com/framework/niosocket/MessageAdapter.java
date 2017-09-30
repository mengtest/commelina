package com.framework.niosocket;

import com.framework.message.BroadcastMessage;
import com.framework.message.NotifyMessage;
import com.framework.message.WorldMessage;
import com.framework.niosocket.proto.SocketMessage;
import com.google.protobuf.Internal;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by @panyao on 2017/8/11.
 */

public final class MessageAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageAdapter.class);

    public static boolean addNotify(Internal.EnumLite domain, NotifyMessage message) {
        SocketMessage msg = MessageResponseProvider.DEFAULT_MESSAGE_PROVIDER
                .createPushMessage(domain, message.getOpcode(), message.getMessage());
        if (msg == null) {
            return false;
        }

        Channel channel = NettyServerContext.Holder.INSTANCE.getUserChannel(message.getUserId());

        ReplyUtils.reply(channel, msg);

        return true;
    }

    public static boolean addBroadcast(Internal.EnumLite domain, BroadcastMessage message) {

        final SocketMessage msg = MessageResponseProvider.DEFAULT_MESSAGE_PROVIDER.createPushMessage(domain, message.getOpcode(), message.getMessage());

        if (msg == null) {
            return false;
        }

        for (Long userId : message.getUserIds()) {
            Channel channel = NettyServerContext.Holder.INSTANCE.getUserChannel(userId);
            ReplyUtils.reply(channel, msg);
        }

        return true;
    }

    public static boolean addWorld(Internal.EnumLite domain, WorldMessage message) {
        final SocketMessage msg = MessageResponseProvider.DEFAULT_MESSAGE_PROVIDER.createPushMessage(domain, message.getOpcode(), message.getMessage());
        if (msg == null) {
            return false;
        }

        for (Long userId : NettyServerContext.Holder.INSTANCE.LOGIN_USERS.values()) {
            Channel channel = NettyServerContext.Holder.INSTANCE.getUserChannel(userId);

            ReplyUtils.reply(channel, msg);
        }

        return true;
    }

}