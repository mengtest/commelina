package com.commelina.niosocket;

import com.commelina.niosocket.message.BroadcastMessage;
import com.commelina.niosocket.message.NotifyMessage;
import com.commelina.niosocket.message.WorldMessage;
import com.commelina.niosocket.proto.MessageBody;
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
    public static void sendNotify(int domain, NotifyMessage message) {
        final SocketMessage msg = SocketMessage.newBuilder()
                .setCode(SERVER_CODE.NOTIFY_CODE)
                .setDomain(domain)
                .setOpcode(message.getOpcode())
                .setBody(MessageBody.newBuilder().setMessage(message.getBody()))
                .build();

        Channel channel = NettyServerContext.INSTANCE.getUserChannel(message.getUserId());

        ReplyUtils.reply(channel, msg);

    }

    /**
     * 添加一个广播消息
     *
     * @param domain
     * @param message
     * @return
     */
    public static void sendBroadcast(int domain, BroadcastMessage message) {
        final SocketMessage msg = SocketMessage.newBuilder()
                .setCode(SERVER_CODE.NOTIFY_CODE)
                .setDomain(domain)
                .setOpcode(message.getOpcode())
                .setBody(MessageBody.newBuilder().setMessage(message.getBody()))
                .build();

        for (Long userId : message.getUserIds()) {
            Channel channel = NettyServerContext.INSTANCE.getUserChannel(userId);
            ReplyUtils.reply(channel, msg);
        }

    }

    /**
     * 发送一个世界消息
     *
     * @param domain
     * @param message
     * @return
     */
    public static void sendWorld(int domain, WorldMessage message) {
        final SocketMessage msg = SocketMessage.newBuilder()
                .setCode(SERVER_CODE.NOTIFY_CODE)
                .setDomain(domain)
                .setOpcode(message.getOpcode())
                .setBody(MessageBody.newBuilder().setMessage(message.getBody()))
                .build();

        // 广播
        NettyServerContext.INSTANCE.CHANNEL_GROUP.writeAndFlush(msg);
    }

}