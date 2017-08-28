package com.nexus.maven.netty.socket;

import com.nexus.maven.core.message.ApiRequest;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by @panyao on 2017/8/28.
 */
public interface RouterContext {

    void doRequestHandler(ChannelHandlerContext ctx, ApiRequest apiRequest);

    void onlineEvent(ChannelHandlerContext ctx);

    void offlineEvent(long logoutUserId, ChannelHandlerContext ctx);

    void exceptionEvent(ChannelHandlerContext ctx, Throwable cause);

}
