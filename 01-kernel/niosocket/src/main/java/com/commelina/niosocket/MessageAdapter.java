package com.commelina.niosocket;

import com.commelina.niosocket.message.BroadcastMessage;
import com.commelina.niosocket.message.NotifyMessage;
import com.commelina.niosocket.message.WorldMessage;
import com.commelina.niosocket.proto.SocketMessage;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息适配器
 *
 * @author @panyao
 * @date 2017/8/11
 */
public final class MessageAdapter {

    private final Logger LOGGER = LoggerFactory.getLogger(MessageAdapter.class);

    /**
     * 发送一个通知消息
     *
     * @param domain
     * @param message
     * @return
     */
    public static boolean sendNotify(int domain, NotifyMessage message) {
        SocketMessage msg = MessageResponseProvider.DEFAULT_MESSAGE_PROVIDER
                .createPushMessage(domain, message.getOpcode(), message.getMessage());
        if (msg == null) {
            return false;
        }

        Channel channel = NettyServerContext.INSTANCE.getUserChannel(message.getUserId());

        ReplyUtils.reply(channel, msg);

        return true;
    }

    /**
     * 添加一个广播消息
     *
     * @param domain
     * @param message
     * @return
     */
    public static boolean sendBroadcast(int domain, BroadcastMessage message) {

        final SocketMessage msg = MessageResponseProvider.DEFAULT_MESSAGE_PROVIDER
                .createPushMessage(domain, message.getOpcode(), message.getMessage());

        if (msg == null) {
            return false;
        }

        for (Long userId : message.getUserIds()) {
            Channel channel = NettyServerContext.INSTANCE.getUserChannel(userId);
            ReplyUtils.reply(channel, msg);
        }

        return true;
    }

    /**
     * 发送一个世界消息
     *
     * @param domain
     * @param message
     * @return
     */
    public static boolean sendWorld(int domain, WorldMessage message) {
        final SocketMessage msg = MessageResponseProvider.DEFAULT_MESSAGE_PROVIDER
                .createPushMessage(domain, message.getOpcode(), message.getMessage());
        if (msg == null) {
            return false;
        }

        for (Long userId : NettyServerContext.INSTANCE.LOGIN_USERS.values()) {
            Channel channel = NettyServerContext.INSTANCE.getUserChannel(userId);
            ReplyUtils.reply(channel, msg);
        }

        return true;
    }

}