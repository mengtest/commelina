package com.commelina.niosocket;

import com.commelina.niosocket.proto.SocketASK;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户消息接收的handler
 *
 * @author @panyao
 * @date 2017/8/24
 */
class ChannelInboundHandlerRouterAdapter extends ChannelInboundHandlerAdapter {

    private final Logger LOGGER = LoggerFactory.getLogger(NettyNioSocketServer.class);

    private SocketEventHandler socketEventHandler;

    /**
     * 当客户端连上服务器的时候会触发此函数
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        final boolean result = NettyServerContext.INSTANCE.channelActive(ctx.channel());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("client:{}, login server: {}", ctx.channel().id(), result);
        }
        socketEventHandler.onOnline(ctx);
    }

    /**
     * 当客户端断开连接的时候触发函数
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final long logoutUserId = NettyServerContext.INSTANCE.channelInactive(ctx.channel());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("client:{}, logout userId:{}", ctx.channel().id(), logoutUserId);
        }
        socketEventHandler.onOffline(ctx, logoutUserId);
    }

    /**
     * 当客户端发送数据到服务器会触发此函数
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final SocketASK ask = (SocketASK) msg;
        // forward = 0 表示心跳
        if (ask.getForward() == 0) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("client id:{}, heartbeat ", ctx.channel().id());
            }
            ctx.writeAndFlush(ProtoBuffStatic.HEARTBEAT_CODE);
        } else {
            try {
                socketEventHandler.onRequest(ctx, ask);
            } catch (Throwable throwable) {
                ctx.writeAndFlush(ProtoBuffStatic.SERVER_ERROR);
                exceptionCaught(ctx, throwable);
            }
        }
    }

    // 调用异常的处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        socketEventHandler.onException(ctx, cause);
    }

//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt instanceof IdleStateEvent) {
//            IdleState state = ((IdleStateEvent) evt).state();
//            if (state == IdleState.READER_IDLE) {
//                // 发现连接是闲置状态就关闭它
//                final long logoutUserId = NettyServerContext.INSTANCE.channelInactive(ctx.channel());
//                LOGGER.info("Idle client:{}, logout userId:{}", ctx.channel().id(), logoutUserId);
//                socketEventHandler.onOffline(logoutUserId, ctx);
//                ctx.close();
//                // throw new Exception("idle exception");
//            }
//        } else {
//            super.userEventTriggered(ctx, evt);
//        }
//    }

    void setHandlers(SocketEventHandler socketEventHandler) {
        this.socketEventHandler = socketEventHandler;
    }

}
