package com.commelina.niosocket;

import com.commelina.niosocket.message.BroadcastMessage;
import com.commelina.niosocket.message.NotifyMessage;
import com.commelina.niosocket.message.WorldMessage;
import com.commelina.niosocket.proto.SERVER_CODE;
import com.commelina.niosocket.proto.SocketMessage;
import io.netty.channel.Channel;

/**
 * 消息适配器
 *
 * @author @panyao
 * @date 2017/8/11
 */
public final class MessageAdapter {

//    private final Logger LOGGER = LoggerFactory.getLogger(MessageAdapter.class);

    /**
     * 发送一个通知消息
     *
     * @param domain
     * @param message
     * @return
     */
    public static boolean sendNotify(int domain, NotifyMessage message) {
        final SocketMessage msg = SocketMessage.newBuilder()
                .setCode(SERVER_CODE.NOTIFY_CODE)
                .setDomain(domain)
                .setOpcode(message.getOpcode())
                .setMsg(message.getBody())
                .build();

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
        final SocketMessage msg = SocketMessage.newBuilder()
                .setCode(SERVER_CODE.NOTIFY_CODE)
                .setDomain(domain)
                .setOpcode(message.getOpcode())
                .setMsg(message.getBody())
                .build();

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
        final SocketMessage msg = SocketMessage.newBuilder()
                .setCode(SERVER_CODE.NOTIFY_CODE)
                .setDomain(domain)
                .setOpcode(message.getOpcode())
                .setMsg(message.getBody())
                .build();

        for (Long userId : NettyServerContext.INSTANCE.LOGIN_USERS.values()) {
            Channel channel = NettyServerContext.INSTANCE.getUserChannel(userId);
            ReplyUtils.reply(channel, msg);
        }

        return true;
    }

}