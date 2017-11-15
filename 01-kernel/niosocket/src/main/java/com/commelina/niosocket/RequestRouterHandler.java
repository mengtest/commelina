package com.commelina.niosocket;

import com.commelina.niosocket.proto.SocketASK;
import io.netty.channel.ChannelHandlerContext;

/**
 * 路由上下文观察者
 *
 * @author @panyao
 * @date 2017/9/22
 */
@Deprecated
public interface RequestRouterHandler {

    /**
     * 路由上下文，当有消息来触发，用来分发消息
     *
     * @param ctx
     * @param request
     */
    void onRequest(ChannelHandlerContext ctx, SocketASK request);

}
