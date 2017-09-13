package com.framework.niosocket;

import com.framework.niosocket.proto.SERVER_CODE;
import com.framework.niosocket.proto.SocketASK;
import com.framework.niosocket.proto.SocketMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by @panyao on 2017/8/24.
 */
class ChannelInboundHandlerRouterAdapter extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyNioSocketServer.class);

    private final NettyServerContext nettyServerContext = NettyServerContext.getInstance();

    private RouterContext routerContext;

    //当客户端连上服务器的时候会触发此函数
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        boolean result = nettyServerContext.channelActive(ctx.channel());
        LOGGER.info("client:{}, login server: {}", ctx.channel().id(), result);
        routerContext.onlineEvent(ctx);
    }

    //当客户端断开连接的时候触发函数
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        long logoutUserId = nettyServerContext.channelInactive(ctx.channel());
        LOGGER.info("client:{}, logout userId:{}", ctx.channel().id(), logoutUserId);
        routerContext.offlineEvent(logoutUserId, ctx);
    }

    //当客户端发送数据到服务器会触发此函数
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SocketASK ask = (SocketASK) msg;
        // 心跳
        if (ask.getApiCode() == 0) {
            LOGGER.info("client id:{}, heartbeat", ctx.channel().id());
            ctx.writeAndFlush(SocketMessage.getDefaultInstance());
        } else {
            routerContext.doRequestHandler(ctx, ask);
        }
    }

    // 调用异常的处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof IOException) {
            long logoutUserId = nettyServerContext.channelInactive(ctx.channel());
            LOGGER.info("client exception:{}, logout userId:{}", ctx.channel().id(), logoutUserId);
        }
        ctx.writeAndFlush(MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE.createErrorMessage(SERVER_CODE.SERVER_ERROR));
        routerContext.exceptionEvent(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                // 发现连接是闲置状态就关闭它
                LOGGER.info("IDLE,关闭了客户端{}的连接", ctx.channel().id());
                ctx.close();
//                throw new Exception("idle exception");
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    void setRouterContext(RouterContext routerContext) {
        this.routerContext = routerContext;
    }

}
