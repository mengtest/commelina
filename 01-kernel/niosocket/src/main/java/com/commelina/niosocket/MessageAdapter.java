package com.commelina.niosocket;

import com.commelina.niosocket.proto.MessageBody;
import com.commelina.niosocket.proto.SERVER_CODE;
import com.commelina.niosocket.proto.SocketMessage;
import com.google.protobuf.ByteString;
import io.netty.channel.Channel;

import java.util.List;

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
     * @param opcode
     * @param userId
     * @param messageBody
     */
    public static void sendNotify(int domain, int opcode, long userId, ByteString messageBody) {
        final SocketMessage msg = SocketMessage.newBuilder()
                .setCode(SERVER_CODE.NOTIFY_CODE)
                .setDomain(domain)
                .setOpcode(opcode)
                .setBody(MessageBody.newBuilder().setMessage(messageBody))
                .build();

        Channel channel = NettyServerContext.INSTANCE.getUserChannel(userId);

        ReplyUtils.reply(channel, msg);

    }

    /**
     * 添加一个广播消息
     *
     * @param domain
     * @param opcode
     * @param userIds
     * @param messageBody
     */
    public static void sendBroadcast(int domain, int opcode, List<Long> userIds, ByteString messageBody) {
        final SocketMessage msg = SocketMessage.newBuilder()
                .setCode(SERVER_CODE.NOTIFY_CODE)
                .setDomain(domain)
                .setOpcode(opcode)
                .setBody(MessageBody.newBuilder().setMessage(messageBody))
                .build();

        for (Long userId : userIds) {
            Channel channel = NettyServerContext.INSTANCE.getUserChannel(userId);
            ReplyUtils.reply(channel, msg);
        }
    }

    /**
     * 发送一个世界消息
     *
     * @param domain
     * @param opcode
     * @param messageBody
     */
    public static void sendWorld(int domain, int opcode, ByteString messageBody) {
        final SocketMessage msg = SocketMessage.newBuilder()
                .setCode(SERVER_CODE.NOTIFY_CODE)
                .setDomain(domain)
                .setOpcode(opcode)
                .setBody(MessageBody.newBuilder().setMessage(messageBody))
                .build();

        // 广播
        NettyServerContext.INSTANCE.CHANNEL_GROUP.writeAndFlush(msg);
    }

    /**
     * 发送错误消息
     *
     * @param domain
     * @param userIds
     * @param messageBody
     */
    public static void sendError(int domain, List<Long> userIds, ByteString messageBody) {
        final SocketMessage msg = SocketMessage.newBuilder()
                .setCode(SERVER_CODE.SERVER_ERROR)
                .setDomain(domain)
                .setBody(MessageBody.newBuilder().setMessage(messageBody))
                .build();

        for (Long userId : userIds) {
            Channel channel = NettyServerContext.INSTANCE.getUserChannel(userId);
            ReplyUtils.reply(channel, msg);
        }
    }

}