package com.framework.niosocket;

import com.framework.message.ResponseMessage;
import com.framework.niosocket.proto.SERVER_CODE;
import com.google.protobuf.Internal;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by @panyao on 2017/9/22.
 */
public final class ReplyUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplyUtils.class);

    public static void reply(ChannelHandlerContext channelHandlerContext, SERVER_CODE serverCode) {
        Object msg = MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE.createErrorMessage(serverCode);
        ChannelFuture future = channelHandlerContext.writeAndFlush(msg);

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
            LOGGER.error("Client cancel receive message.");
        }
    }

    public static void reply(ChannelHandlerContext channelHandlerContext, Internal.EnumLite domain, Internal.EnumLite opcode, ResponseMessage responseMessage) {

        Object msg = MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE.createResponseMessage(domain, opcode, responseMessage.getMessage());

        ChannelFuture future = channelHandlerContext.writeAndFlush(msg);

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
