package com.nexus.maven.netty.socket;

import com.nexus.maven.proto.SYSTEM_CODE_CONSTANTS;
import com.nexus.maven.proto.SocketASK;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.logging.Logger;

/**
 * Created by @panyao on 2017/8/24.
 */
class ChannelInboundHandlerRouterContextAdapter extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = Logger.getLogger(NettyNioSocketServer.class.getName());
    private final NettyServerContext nettyServerContext = NettyServerContext.getInstance();

    private RouterContext routerContext;

    //当客户端连上服务器的时候会触发此函数
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        boolean result = nettyServerContext.channelActive(ctx.channel());
        LOGGER.info("client:" + ctx.channel().id() + ", login server:" + result);
        routerContext.onlineEvent(ctx);
    }

    //当客户端断开连接的时候触发函数
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        long logoutUserId = nettyServerContext.channelInactive(ctx.channel());
        LOGGER.info("client:" + ctx.channel().id() + ", logout userId:" + logoutUserId);
        routerContext.offlineEvent(logoutUserId, ctx);
    }

    //当客户端发送数据到服务器会触发此函数
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 协议格式错误
        if (!(msg instanceof SocketASK)) {
            // TODO: 2017/8/29 output 这里可以再优化一下
            ctx.writeAndFlush(MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE
                    .createErrorMessage(SYSTEM_CODE_CONSTANTS.PROTOCOL_FORMAT_ERROR_VALUE));
            return;
        }
        routerContext.doRequestHandler(ctx, (SocketASK) msg);
    }

    // 调用异常的处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        routerContext.exceptionEvent(ctx, cause);
        ctx.close();
    }

    void setRouterContext(RouterContext routerContext) {
        this.routerContext = routerContext;
    }

}
