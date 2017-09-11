package com.game.robot.interfaces;

import com.framework.niosocket.proto.SocketMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by @panyao on 2017/9/11.
 */
public interface SocketHandler {

    void channelRead(ChannelHandlerContext ctx, SocketMessage msg);

    void connectSuccess(ChannelHandlerContext ctx);

    default void disconnect(ChannelHandlerContext ctx) {

    }

    default void heartError(ChannelHandlerContext ctx) {

    }

}
