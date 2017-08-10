package com.nexus.maven.netty;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by @panyao on 2017/8/7.
 */
public interface RPCRouterDispatchInterface {

    /**
     * 传入的参数
     * 响应的object
     *
     * @param jsonMessage
     * @return
     */
    void invoke(ChannelHandlerContext ctx, Object jsonMessage);

}
