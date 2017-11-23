package com.commelina.math24.play.robot.niosocket;

import com.commelina.niosocket.proto.*;
import com.commelina.utils.Version;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author @panyao
 * @date 2017/9/7
 */
public class BusinessRouterHandler extends ChannelInboundHandlerAdapter {

    private Logger LOGGER = LoggerFactory.getLogger(BusinessRouterHandler.class);

    private final MemberEventLoop eventLoop;

    public BusinessRouterHandler(MemberEventLoop loop) {
        eventLoop = loop;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("与服务端建立连接");

        ctx.writeAndFlush(SocketASK.newBuilder()
                .setForward(SYSTEM_FORWARD_CODE.SYSTEM_VALUE)
                .setBody(RequestBody.newBuilder()
                        .setOpcode(SYSTEM_OPCODE.LOGIN_CODE_VALUE)
                        .setVercode(Version.create("1.0.0"))
                )
                .build());

        LOGGER.info("向服务端发送登录请求");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("服务端断开连接");
        eventLoop.ctx = null;
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SocketMessage message = (SocketMessage) msg;
        switch (message.getCode()) {
            case NOTIFY_CODE:
                LOGGER.info("收到服务端通知消息 {}", message.getClass());
                eventLoop.acceptor(message);
                break;
            case RESONSE_CODE:
                LOGGER.info("收到服务端回复消息 {}", message.getClass());
                eventLoop.acceptor(message);
                break;
            case UNAUTHORIZED:
                LOGGER.error("需要登录才能访问");
                eventLoop.ctx = null;
                break;
            case SERVER_ERROR:
                LOGGER.error("服务端内部错误");
                if (message.getBody() != null) {
                    LOGGER.error("Debug error message : {}", message.getBody());
                }
                break;
            case HEARTBEAT_CODE:
                LOGGER.debug("heartbeat");
                break;
            default:
                throw new RuntimeException("Undefined type " + message.getCode());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("channelId:{}, error:{}", ctx.channel().id().asLongText(), cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                // Write heartbeat to server
                LOGGER.debug("Write heartbeat to server. Time: " + System.currentTimeMillis());
                ctx.writeAndFlush(SocketASK.getDefaultInstance());
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }
    }

}
