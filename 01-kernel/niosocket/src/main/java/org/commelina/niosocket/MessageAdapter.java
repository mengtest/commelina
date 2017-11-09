package org.commelina.niosocket;

import org.commelina.niosocket.message.NotifyMessage;
import org.commelina.niosocket.message.WorldMessage;
import org.commelina.niosocket.message.BroadcastMessage;
import org.commelina.niosocket.proto.SocketMessage;
import com.google.protobuf.Internal;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageAdapter.class);

    /**
     * 发送一个通知消息
     *
     * @param domain
     * @param message
     * @return
     */
    public static boolean sendNotify(Internal.EnumLite domain, NotifyMessage message) {
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
    public static boolean sendBroadcast(Internal.EnumLite domain, BroadcastMessage message) {

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
    public static boolean sendWorld(Internal.EnumLite domain, WorldMessage message) {
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