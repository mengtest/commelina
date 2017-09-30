package com.framework.niosocket;

import com.framework.niosocket.proto.SocketASK;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by @panyao on 2017/8/24.
 */
class ChannelInboundHandlerRouterAdapter extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyNioSocketServer.class);

    private RouterContextHandler routerContextHandlerImpl;

    private MemberEventHandler memberEventHandler = new MemberEventHandler() {
    };

    //当客户端连上服务器的时候会触发此函数
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final boolean result = NettyServerContext.INSTANCE.channelActive(ctx.channel());
        LOGGER.info("client:{}, login server: {}", ctx.channel().id(), result);
        memberEventHandler.onOnline(ctx);
    }

    //当客户端断开连接的时候触发函数
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final long logoutUserId = NettyServerContext.INSTANCE.channelInactive(ctx.channel());
        LOGGER.info("client:{}, logout userId:{}", ctx.channel().id(), logoutUserId);
        memberEventHandler.onOffline(logoutUserId, ctx);
    }

    //当客户端发送数据到服务器会触发此函数
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final SocketASK ask = (SocketASK) msg;
        // 心跳
        if (ask.getOpcode() == 0) {
            LOGGER.info("client id:{}, heartbeat", ctx.channel().id());
            ctx.writeAndFlush(ProtoBuffMap.HEARTBEAT_CODE);
        } else {
            routerContextHandlerImpl.onRequest(ctx, ask);
        }
    }

    // 调用异常的处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        if (cause instanceof IOException) {
//            final long logoutUserId = nettyServerContext.channelInactive(ctx.channel());
//            LOGGER.info("client exception:{}, logout userId:{}", ctx.channel().id(), logoutUserId);
//        }
        ctx.writeAndFlush(ProtoBuffMap.SERVER_ERROR);
        memberEventHandler.onException(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                // 发现连接是闲置状态就关闭它
                final long logoutUserId = NettyServerContext.INSTANCE.channelInactive(ctx.channel());
                LOGGER.info("Idle client:{}, logout userId:{}", ctx.channel().id(), logoutUserId);
                memberEventHandler.onOffline(logoutUserId, ctx);
                ctx.close();
//                throw new Exception("idle exception");
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    void setHandlers(RouterContextHandler routerContextHandlerImpl, MemberEventHandler memberEventHandler) {
        this.routerContextHandlerImpl = routerContextHandlerImpl;
        if (memberEventHandler != null) {
            this.memberEventHandler = memberEventHandler;
        }
    }

}
