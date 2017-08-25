package com.nexus.maven.netty.socket;

import com.nexus.maven.core.message.ApiRequest;
import com.nexus.maven.core.message.RequestArg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.socket.netty.proto.SocketNettyProtocol;

import java.util.logging.Logger;

/**
 * Created by @panyao on 2017/8/24.
 */
class ChannelInboundHandlerActorAkkaAdapter extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = Logger.getLogger(NettyNioSocketServer.class.getName());
    private final NettyServerContext nettyServerContext = NettyServerContext.getInstance();

    private ActorAkkaContext actorAkkaContext;

    //当客户端连上服务器的时候会触发此函数
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        boolean result = nettyServerContext.channelActive(ctx.channel());
        LOGGER.info("client:" + ctx.channel().id() + ", login server:" + result);
    }

    //当客户端断开连接的时候触发函数
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        long logoutUserId = nettyServerContext.channelInactive(ctx.channel());
        actorAkkaContext.unRegister(ctx);
        LOGGER.info("client:" + ctx.channel().id() + ", logout userId:" + logoutUserId);
    }

    //当客户端发送数据到服务器会触发此函数
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 协议格式错误
        if (!(msg instanceof SocketNettyProtocol.SocketASK)) {
            ctx.writeAndFlush(MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE
                    .createErrorMessage(SocketNettyProtocol.SYSTEM_CODE_CONSTANTS.PROTOCOL_FORMAT_ERROR_VALUE));
            return;
        }
        SocketNettyProtocol.SocketASK request = (SocketNettyProtocol.SocketASK) msg;
        RequestArg[] args = new RequestArg[request.getArgsList().size()];
        for (int i = 0; i < request.getArgsList().size(); i++) {
            SocketNettyProtocol.Arg arg = request.getArgsList().get(i);
            args[i] = new RequestArg(arg.getValue(), RequestArg.DATA_TYPE.valueOf(arg.getDataType().name()));
        }

        actorAkkaContext.apiRouter(ctx, new ApiRequest(request.getApiPath(), request.getVersion(), args));
    }

    // 调用异常的处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public void setActorAkkaContext(ActorAkkaContext actorAkkaContext) {
        this.actorAkkaContext = actorAkkaContext;
    }

}
