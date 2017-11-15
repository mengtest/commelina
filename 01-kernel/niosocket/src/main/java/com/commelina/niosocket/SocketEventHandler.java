package com.commelina.niosocket;

import com.commelina.niosocket.proto.SocketASK;
import io.netty.channel.ChannelHandlerContext;

/**
 * 用户事件
 *
 * @author @panyao
 * @date 2017/8/28
 */
public interface SocketEventHandler {

    /**
     * 路由上下文，当有消息来触发，用来分发消息
     *
     * @param ctx
     * @param ask
     */
    void onRequest(ChannelHandlerContext ctx, SocketASK ask);

    /**
     * 用户上线
     *
     * @param ctx
     */
    default void onOnline(ChannelHandlerContext ctx) {

    }

    /**
     * 用户离线
     *
     * @param ctx
     * @param logoutUserId
     */
    default void onOffline(ChannelHandlerContext ctx, long logoutUserId) {

    }

    /**
     * 异常
     *
     * @param ctx
     * @param cause
     */
    default void onException(ChannelHandlerContext ctx, Throwable cause) {
        throw new RuntimeException(cause);
    }

}