package com.framework.niosocket;

import com.framework.message.*;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by @panyao on 2017/9/22.
 */
@Deprecated
public final class ReplyUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplyUtils.class);

    public static void reply(ChannelHandlerContext channelHandlerContext, ResponseMessageDomain message) {
        ChannelFuture future = channelHandlerContext.writeAndFlush(
                MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE.createResponseMessage(message.getDomain(), message.getMessage().getOpcode().getNumber(), message.getMessage().getMessage()));

        if (future.isSuccess()) {
            // 成功
        } else if (future.cause() != null) {
            // FIXME: 2017/8/8 全部转换为领域模型
            // 异常
            //  throw new Exception(future.cause());
            LOGGER.error("{}", future.cause());
        } else {
            // 取消
            //  throw new Exception("客户端取消执行");
            LOGGER.error("client cancel receive message.");
        }
    }

}
