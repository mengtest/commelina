package com.game.robot.interfaces;

import com.framework.niosocket.proto.SocketMessage;
import com.game.robot.niosocket.NettyClient;
import com.google.common.collect.Lists;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by @panyao on 2017/9/11.
 */
public final class MainHandler implements MainGameEvent {

    private Logger LOGGER = LoggerFactory.getLogger(MainHandler.class);

    private int errorTimes = 0;

    private final MemberEventLoop memberEventLoop = new MemberEventLoop();

    private final List<HandlerEvent> initHandlers = Lists.newArrayList();

    public void channelRead(ChannelHandlerContext ctx, SocketMessage msg) {
        memberEventLoop.acceptor(ctx, msg);
    }

    public void connectSuccess(ChannelHandlerContext ctx) {
        memberEventLoop.ctx = ctx;
        // 执行启动事件
        for (HandlerEvent initHandler : initHandlers) {
            memberEventLoop.addEvent(initHandler);
        }
    }

    public void disconnect(ChannelHandlerContext ctx) {
        // 优雅关机
        memberEventLoop.eventLoop.shutdownGracefully();
    }

    public void heartError(ChannelHandlerContext ctx) {
        disconnect(ctx);
    }

    public void start(HandlerEvent event, HandlerEvent... events) {
        initHandlers.add(event);
        Collections.addAll(initHandlers, events);

        // 根据账号获取 token
        memberEventLoop.isReady = NettyClient.getNettyClientByConfig(this);
    }

    @Override
    public void exception(ChannelHandlerContext ctx, Throwable throwable) {
        if (throwable instanceof IOException) {
            memberEventLoop.isReady = false;
            if (errorTimes++ < 10) {
                try {
                    Thread.sleep(2000);
                    LOGGER.info("第{}次尝试重连,{}", errorTimes, LocalTime.now().withNano(0));
                    memberEventLoop.isReady = NettyClient.getNettyClientByConfig(this);
                } catch (InterruptedException e) {
                    LOGGER.error("thread error, {}.", e);
                    exception(ctx, throwable);
                }
            }
        }
    }

}
