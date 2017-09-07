package com.framework.niosocket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by @panyao on 2017/8/24.
 */
@ChannelHandler.Sharable
class ChannelInboundHandlerAcceptorIdleStateTrigger extends ChannelInboundHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(ChannelInboundHandlerAcceptorIdleStateTrigger.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                // 发现连接是闲置状态就关闭它
                logger.info("关闭了客户端{}的连接", ctx.channel().id());
//                ctx.close();
                throw new Exception("idle exception");
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
