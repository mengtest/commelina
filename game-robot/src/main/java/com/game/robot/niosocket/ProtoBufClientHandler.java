package com.game.robot.niosocket;

import com.framework.niosocket.proto.SERVER_CODE;
import com.framework.niosocket.proto.SocketASK;
import com.framework.niosocket.proto.SocketMessage;
import com.game.robot.interfaces.SocketHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by @panyao on 2017/9/7.
 */
public class ProtoBufClientHandler extends ChannelInboundHandlerAdapter {

    private Logger LOGGER = LoggerFactory.getLogger(ProtoBufClientHandler.class);

    private final SocketHandler socketHandler;

    public ProtoBufClientHandler(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("连接建立成功");
        socketHandler.connectSuccess(ctx);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        socketHandler.disconnect(ctx);
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.info("收到服务端的消息");
        SocketMessage message = (SocketMessage) msg;
        if (message.getCode() == SERVER_CODE.HEARTBEAT_CODE) {
            LOGGER.info("心跳正常");
            socketHandler.heartError(ctx);
            return;
        }
        socketHandler.channelRead(ctx, message);
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
//            IdleState state = ((IdleStateEvent) evt).state();
//            if (state == IdleState.WRITER_IDLE) {
            // write heartbeat to server
            System.out.println("write heartbeat to server. time: " + System.currentTimeMillis());
            ctx.writeAndFlush(SocketASK.getDefaultInstance());
//            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
