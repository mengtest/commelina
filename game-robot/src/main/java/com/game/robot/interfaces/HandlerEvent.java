package com.game.robot.interfaces;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by @panyao on 2017/9/11.
 */
public interface HandlerEvent {

    void handle(MemberEventLoop eventLoop, ChannelHandlerContext ctx);

}
