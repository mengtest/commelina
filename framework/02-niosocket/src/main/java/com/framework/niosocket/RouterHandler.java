package com.framework.niosocket;

import com.framework.niosocket.proto.SocketASK;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by @panyao on 2017/8/28.
 */
public interface RouterHandler {

    void doRequestHandler(ChannelHandlerContext ctx, SocketASK request);

    void onlineEvent(ChannelHandlerContext ctx);

    void offlineEvent(long logoutUserId, ChannelHandlerContext ctx);

    void exceptionEvent(ChannelHandlerContext ctx, Throwable cause);

}
