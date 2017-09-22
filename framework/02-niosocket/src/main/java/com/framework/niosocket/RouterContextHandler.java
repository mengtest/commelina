package com.framework.niosocket;

import com.framework.niosocket.proto.SocketASK;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by @panyao on 2017/9/22.
 */
public interface RouterContextHandler {

    void onRequest(ChannelHandlerContext ctx, SocketASK request);

}
