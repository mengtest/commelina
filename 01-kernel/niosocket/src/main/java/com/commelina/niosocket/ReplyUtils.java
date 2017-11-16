package com.commelina.niosocket;

import com.commelina.niosocket.proto.SERVER_CODE;
import com.commelina.niosocket.proto.SocketMessage;
import com.google.protobuf.ByteString;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 响应的utils
 *
 * @author @panyao
 * @date 2017/9/22
 */
public final class ReplyUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplyUtils.class);

    /**
     * 回复消息到客户端
     *
     * @param channel
     * @param socketMessage
     */
    private static void reply(Channel channel, SocketMessage socketMessage) {
        ChannelFuture future = channel.writeAndFlush(socketMessage);

        if (future.isSuccess()) {
            // 成功
        } else if (future.cause() != null) {
            // FIXME: 2017/8/8 全部转换为领域模型
            // 异常
            LOGGER.error("{}", future.cause());
        } else {
            // 取消
            //  throw new Exception("客户端取消执行");
            LOGGER.error("Client cancel receive message.");
        }
    }

    /**
     * 回复消息到客户端
     *
     * @param channelHandlerContext
     * @param domain
     * @param opcode
     * @param message
     */
    public static void reply(ChannelHandlerContext channelHandlerContext,
                             int domain,
                             int opcode,
                             ByteString message) {
        final SocketMessage msg = SocketMessage.newBuilder()
                .setCode(SERVER_CODE.RESONSE_CODE)
                .setDomain(domain)
                .setOpcode(opcode)
                .setMsg(message)
                .build();
        reply(channelHandlerContext.channel(), msg);
    }

    /**
     * 回复消息到客户端
     *
     * @param channelHandlerContext
     * @param domain
     * @param opcode
     */
    public static void reply(ChannelHandlerContext channelHandlerContext,
                             int domain,
                             int opcode) {
        final SocketMessage msg = SocketMessage.newBuilder()
                .setCode(SERVER_CODE.RESONSE_CODE)
                .setDomain(domain)
                .setOpcode(opcode)
                .build();
        reply(channelHandlerContext.channel(), msg);
    }

}
