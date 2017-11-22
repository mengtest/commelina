package com.commelina.niosocket;

import com.commelina.niosocket.proto.*;
import com.google.protobuf.ByteString;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

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
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("client:{}, connection.", ctx.channel().id());
//        }
//    }

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
        if (logoutUserId > 0) {
            socketEventHandler.onOffline(ctx, logoutUserId);
        }
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
        if (ask.getForward() >= SYSTEM_FORWARD_CODE.BOUNDARY_VALUE) {
            onRequest(ctx, ask);
            return;
        }

        if (ask.getForward() == SYSTEM_FORWARD_CODE.HEARTBEAT_VALUE) {
            heartbeat(ctx, ask);
            return;
        }

        systemOption(ctx, ask);
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

    private void onRequest(ChannelHandlerContext ctx, SocketASK ask) throws Exception {
        long userId = NettyServerContext.INSTANCE.getLoginUserId(ctx.channel().id());
        if (userId <= 0) {
            ctx.writeAndFlush(ProtoBuffStatic.UNAUTHORIZED);
            return;
        }
        try {
            socketEventHandler.onRequest(ctx, userId, ask);
        } catch (Throwable throwable) {
            if (LOGGER.isDebugEnabled()) {
                final SocketMessage message = SocketMessage.newBuilder()
                        .setCode(SERVER_CODE.SERVER_ERROR)
                        .setDomain(ask.getForward())
                        .setBody(MessageBody.newBuilder().setMessage(ByteString.copyFromUtf8(throwable.getLocalizedMessage())))
                        .build();
                ctx.writeAndFlush(message);
            } else {
                ctx.writeAndFlush(ProtoBuffStatic.SERVER_ERROR);
                exceptionCaught(ctx, throwable);
            }
        }
    }

    private void systemOption(ChannelHandlerContext ctx, SocketASK ask) {
        switch (ask.getBody().getOpcode()) {
            case SYSTEM_OPCODE.LOGIN_CODE_VALUE:
                login(ctx, ask);
                break;
            default:
                // nothing to do
        }
    }

    private void heartbeat(ChannelHandlerContext ctx, SocketASK ask) {
        // forward = 0 表示心跳
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("client id:{}, heartbeat ", ctx.channel().id());
        }
        ctx.writeAndFlush(ProtoBuffStatic.HEARTBEAT);
    }

    private void login(ChannelHandlerContext ctx, SocketASK ask) {
        CompletableFuture<Long> userIdCompletableFuture = socketEventHandler.onLogin(ctx, ask);
        do {
            if (userIdCompletableFuture == null) {
                break;
            }
            long userId = userIdCompletableFuture.join();
            if (userId <= 0) {
                break;
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Login success, channelId {}, userId {} ", ctx.channel().id(), userId);
            }
            NettyServerContext.INSTANCE.userJoin(ctx.channel(), userId);
            ctx.writeAndFlush(ProtoBuffStatic.LOGIN_SUCCESS);
            return;
        } while (false);
        ctx.writeAndFlush(ProtoBuffStatic.LOGIN_FAILED);
    }

}
