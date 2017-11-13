package com.commelina.math24.play.robot.interfaces;

import com.github.freedompy.commelina.niosocket.proto.SocketMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author @panyao
 * @date 2017/9/11
 */
public interface SocketHandler {

    /**
     * 服务端有消息下来
     *
     * @param ctx
     * @param msg
     */
    void channelRead(ChannelHandlerContext ctx, SocketMessage msg);

    /**
     * 连接成功
     *
     * @param ctx
     */
    void connectSuccess(ChannelHandlerContext ctx);

    /**
     * 断开连接
     *
     * @param ctx
     */
    default void disconnect(ChannelHandlerContext ctx) {

    }

    /**
     * 有异常
     *
     * @param ctx
     * @param throwable
     */
    default void exception(ChannelHandlerContext ctx, Throwable throwable) {
        throw new RuntimeException(throwable);
    }

}
