package com.nexus.maven.netty.socket;

import com.google.protobuf.ByteString;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.socket.netty.proto.SocketNettyProtocol;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by @panyao on 2017/8/11.
 */
class MessageNotifyHandlerWithProtoBuff {

    public NettyServerContext context;

    private final Logger logger = Logger.getLogger(MessageNotifyHandlerWithProtoBuff.class.getName());

    public void addNotify(MessageNotifyHandler messageHandler) throws IOException {
        byte[] bytes = messageHandler.getBytes();
        if (bytes == null) {
            throw new IOException("serialize failed.");
        }

        long userId = messageHandler.getUserId();

        Channel channel;
        try {
            channel = context.getUserChannel(userId);
        } catch (UserUnLoginException | UserChannelNotFoundException e) {
            if (messageHandler.getListener() != null) {
                messageHandler.getListener().call(PipelineNotifyFuture.STATUS_CODE.USER_UN_LOGIN, null);
            } else {
                logger.info(String.format("userId:%s, 未登陆，消息被忽略", userId));
            }
            return;
        } catch (UserChannelUnActiveException e) {
            if (messageHandler.getListener() != null) {
                messageHandler.getListener().call(PipelineNotifyFuture.STATUS_CODE.USER_CHANNEL_UN_ACTIVE, null);
            } else {
                logger.info(String.format("userId:%s, channel下线，消息被忽略", userId));
            }
            return;
        }

        if (channel == null) {
            if (messageHandler.getListener() != null) {
                messageHandler.getListener().call(PipelineNotifyFuture.STATUS_CODE.UNDEFIEND_ERRROR, null);
            } else {
                logger.info(String.format("userId:%s, 未知错误，消息被忽略", userId));
            }
            return;
        }

        SocketNettyProtocol.SocketMessage notifyMessage = SocketNettyProtocol.SocketMessage.newBuilder()
                .setCode(SocketNettyProtocol.SYSTEM_CODE_CONSTANTS.NOTIFY_CODE_VALUE)
                .setMsg(SocketNettyProtocol.BusinessMessage.newBuilder()
                        .setOpCode(messageHandler.getOpCode())
                        .setVersion(messageHandler.getVersion())
                        .setBp(messageHandler.getBp())
                        .setMsg(ByteString.copyFrom(bytes))
                ).build();

        ChannelFuture future = channel.writeAndFlush(notifyMessage);
        if (future.isDone()) {
            if (future.isSuccess()) {

            } else if (future.cause() != null) {
                // 异常
                logger.info(String.format("userId:%s,异常%s", userId, future.cause().getMessage()));
            } else {
                // 取消
                logger.info(String.format("userId:%s,取消", userId));
            }
            if (messageHandler.getListener() != null) {
                messageHandler.getListener().call(PipelineNotifyFuture.STATUS_CODE.UNDEFIEND_ERRROR, future);
            }
        } else {
            throw new RuntimeException("这里是调试的错误，发现了就改了。");
        }

    }

}
