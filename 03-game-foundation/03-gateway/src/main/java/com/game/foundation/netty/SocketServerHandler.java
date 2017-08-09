package com.game.foundation.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.logging.Logger;

/**
 * Created by @panyao on 2017/8/9.
 */
@Component
public class SocketServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = Logger.getLogger(SocketServerHandler.class.getName());

    @Resource
    private NettyServerContext nettyServerContext;

    @Resource
    private RPCRouterDispatchInterface dispatch;

    //当客户端连上服务器的时候会触发此函数
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        boolean result = nettyServerContext.channelActive(ctx.channel());
        LOGGER.info("client:" + ctx.channel().id() + ", login server:" + result);
    }

    //当客户端断开连接的时候触发函数
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        long logoutUserId = nettyServerContext.channelInactivee(ctx.channel());
        LOGGER.info("client:" + ctx.channel().id() + ", logout userId:" + logoutUserId);
    }

    //当客户端发送数据到服务器会触发此函数
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 当前 channel 未登录
        this.dispatch.invoke(ctx, msg);
    }

    // 调用异常的处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
