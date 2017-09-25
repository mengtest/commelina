package com.framework.niosocket;

import com.framework.message.ApiRequest;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by @panyao on 2017/9/6.
 */
public interface RequestHandler {

    void onRequest(ApiRequest request, ChannelHandlerContext ctx);

}
