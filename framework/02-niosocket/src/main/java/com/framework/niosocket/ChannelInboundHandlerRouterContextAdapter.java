package com.framework.niosocket;

import com.framework.niosocket.proto.SERVER_CODE;
import com.framework.niosocket.proto.SocketASK;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by @panyao on 2017/8/24.
 */
class ChannelInboundHandlerRouterContextAdapter extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyNioSocketServer.class);

    private final NettyServerContext nettyServerContext = NettyServerContext.getInstance();

    private RouterContext routerContext;

    //当客户端连上服务器的时候会触发此函数
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        boolean result = nettyServerContext.channelActive(ctx.channel());
        LOGGER.info("client:{}, login server: {}", ctx.channel().id(), result);
//        routerContext.onlineEvent(ctx);
    }

    //当客户端断开连接的时候触发函数
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        long logoutUserId = nettyServerContext.channelInactive(ctx.channel());
        LOGGER.info("client:{}, logout userId:{}", ctx.channel().id(), logoutUserId);
//        routerContext.offlineEvent(logoutUserId, ctx);
    }

    //当客户端发送数据到服务器会触发此函数
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 协议格式错误
        if (LOGGER.isDebugEnabled() && !(msg instanceof SocketASK)) {
            // TODO: 2017/8/29 output 这里可以再优化一下
            ctx.writeAndFlush(MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE
                    .createErrorMessage(SERVER_CODE.PROTOCOL_FORMAT_ERROR));
            return;
        }
        SocketASK ask = (SocketASK) msg;
        if (ask.getIsHeartbeat()) {
            ctx.writeAndFlush(MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE
                    .createErrorMessage(SERVER_CODE.HEARTBEAT_CODE));
        } else {
//            routerContext.doRequestHandler(ctx, ask.getRequest());
        }
    }

    // 调用异常的处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof IOException) {
            long logoutUserId = nettyServerContext.channelInactive(ctx.channel());
            LOGGER.info("client exception:{}, logout userId:{}", ctx.channel().id(), logoutUserId);
        }
//        routerContext.exceptionEvent(ctx, cause);
        ctx.close();
    }

    void setRouterContext(RouterContext routerContext) {
        this.routerContext = routerContext;
    }

}
