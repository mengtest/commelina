package com.game.robot;

import com.framework.niosocket.proto.SocketASK;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by @panyao on 2017/9/7.
 */
public class ProtoBufClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接建立成功");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到服务端的消息");

        ChannelFuture future = ctx.channel().writeAndFlush(SocketASK.getDefaultInstance());
        System.out.println("回复成功" + future.isSuccess());

        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // write heartbeat to server
            System.out.println("write heartbeat to server. time: " + System.currentTimeMillis());
            ctx.writeAndFlush(SocketASK.getDefaultInstance());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
