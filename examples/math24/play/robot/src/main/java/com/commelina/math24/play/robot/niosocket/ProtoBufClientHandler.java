package com.commelina.math24.play.robot.niosocket;

import com.commelina.math24.play.robot.interfaces.SocketHandler;
import com.commelina.niosocket.proto.SERVER_CODE;
import com.commelina.niosocket.proto.SocketASK;
import com.commelina.niosocket.proto.SocketMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author @panyao
 * @date 2017/9/7
 */
public class ProtoBufClientHandler extends ChannelInboundHandlerAdapter {

    private Logger LOGGER = LoggerFactory.getLogger(ProtoBufClientHandler.class);

    private final SocketHandler socketHandler;

    public ProtoBufClientHandler(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("Connection success.");
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
        LOGGER.debug("Received message.");
        SocketMessage message = (SocketMessage) msg;
        if (message.getCode() == SERVER_CODE.HEARTBEAT_CODE) {
            LOGGER.debug("Heartbeat normal.");
            // nothing to do
            return;
        }

        socketHandler.channelRead(ctx, message);
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        socketHandler.exception(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
//            IdleState state = ((IdleStateEvent) evt).state();
//            if (state == IdleState.WRITER_IDLE) {
            // Write heartbeat to server
            LOGGER.debug("Write heartbeat to server. Time: " + System.currentTimeMillis());
            ctx.writeAndFlush(SocketASK.getDefaultInstance());
//            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
