package com.nexus.maven.netty.socket;

import com.nexus.maven.core.message.ResponseMessage;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

/**
 * Created by @panyao on 2017/8/25.
 */
public class ChannelOutputHandler {

    private final Logger LOGGER = Logger.getLogger(ChannelOutputHandler.class.getName());

    ChannelHandlerContext channelHandlerContext;

    public void writeAndFlush(int domain, ResponseMessage message) {
        ChannelFuture future = channelHandlerContext.writeAndFlush(
                MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE.createResponseMessage(domain, message.getOpcode().getNumber(), message.getMessage()));

        if (future.isSuccess()) {
            // 成功
        } else if (future.cause() != null) {
            // FIXME: 2017/8/8 全部转换为领域模型
            // 异常
            //  throw new Exception(future.cause());
            LOGGER.info(future.cause().getMessage());
        } else {
            // 取消
            //  throw new Exception("客户端取消执行");
            LOGGER.info("client cancel receive message.");
        }
    }

    public ChannelHandlerContext getRawContext() {
        return this.channelHandlerContext;
    }

}
