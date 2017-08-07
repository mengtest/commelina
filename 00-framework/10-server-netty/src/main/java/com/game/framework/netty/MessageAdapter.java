package com.game.framework.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.logging.Logger;

/**
 * Created by @panyao on 2017/8/7.
 */
@Component
public class MessageAdapter {

    @Resource
    private NettyServerContext context;

    private static final Logger logger = Logger.getLogger(GameServer.class.getName());

    public void addMessage(MessageEntity entity) {
        Channel channel = null;
        try {
            channel = context.getUserChannel(entity.getUserId());
        } catch (UserUnLoginException e) {
            logger.info(String.format("userId:%s, 未登陆，消息被忽略", entity.getUserId()));
            return;
        } catch (UserChannelNotFoundException e) {
            logger.info(String.format("userId:%s, 下线，消息被忽略", entity.getUserId()));
            return;
        } catch (Throwable throwable) {
            logger.info(String.format("未知错误，消息被忽略,%s", throwable.getMessage()));
            return;
        }

        if (channel != null) {
            ChannelFuture future = channel.writeAndFlush(entity.getMsg());
            if (future.isDone()) {
                // 成功
                if (future.isSuccess()) {

                } else if (future.cause() != null) {
                    // 异常
                    logger.info(String.format("userId:%s,异常%s", entity.getUserId(), future.cause().getMessage()));
                    // FIXME: 2017/8/7 再次投递
                } else {
                    // 取消
                    logger.info(String.format("userId:%s,取消", entity.getUserId()));
                }
            }
        } else {
            logger.info(String.format("userId:%s, 未知错误，消息被忽略", entity.getUserId()));
        }
    }

}


