package com.framework.niosocket;

import com.framework.niosocket.proto.SocketASK;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by @panyao on 2017/9/6.
 */
public interface RequestHandler {

    void onRequest(SocketASK ask, ChannelHandlerContext ctx);

}
