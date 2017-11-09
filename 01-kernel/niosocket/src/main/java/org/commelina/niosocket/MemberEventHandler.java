package org.commelina.niosocket;

import io.netty.channel.ChannelHandlerContext;

/**
 * 用户事件
 *
 * @author @panyao
 * @date 2017/8/28
 */
public interface MemberEventHandler {

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
     * @param logoutUserId
     * @param ctx
     */
    default void onOffline(long logoutUserId, ChannelHandlerContext ctx) {

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