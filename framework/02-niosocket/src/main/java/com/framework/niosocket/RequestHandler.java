package com.framework.niosocket;

import com.framework.niosocket.proto.SocketASK;
import io.netty.channel.ChannelHandlerContext;

/**
 * 请求的观察者
 *
 * @author @panyao
 * @date 2017/9/6
 */
public interface RequestHandler {

    /**
     * 当有新请求到达触发
     *
     * @param ask
     * @param ctx
     */
    void onRequest(SocketASK ask, ChannelHandlerContext ctx);

}
