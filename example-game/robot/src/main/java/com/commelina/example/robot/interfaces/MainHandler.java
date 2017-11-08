package com.commelina.example.robot.interfaces;

import com.github.freedompy.commelina.niosocket.proto.SocketMessage;
import com.commelina.example.robot.niosocket.NettyClient;
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

    private final List<MemberEvent> initHandlers = Lists.newArrayList();

    public void channelRead(ChannelHandlerContext ctx, SocketMessage msg) {
        memberEventLoop.acceptor(msg);
    }

    public void connectSuccess(ChannelHandlerContext ctx) {
        LOGGER.info("成功连接服务器");
        errorTimes = 0;
        memberEventLoop.isReady = true;

        memberEventLoop.ctx = ctx;
        // 执行启动事件
        for (MemberEvent initHandler : initHandlers) {
            memberEventLoop.addEvent(initHandler);
        }
    }

    public void disconnect(ChannelHandlerContext ctx) {
        // 优雅关机
        memberEventLoop.eventLoop.shutdownGracefully();
    }

    public void start(MemberEvent event, MemberEvent... events) {
        initHandlers.add(event);
        Collections.addAll(initHandlers, events);

        // 根据账号获取 token
        do {
            memberEventLoop.isReady = NettyClient.getNettyClientByConfig(this);
            if (!memberEventLoop.isReady) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    LOGGER.error("thread error, {}.", e);
                }
                LOGGER.info("尝试再次连接服务器,直到服务器启动");
            }
        } while (!memberEventLoop.isReady);
    }

    @Override
    public void exception(ChannelHandlerContext ctx, Throwable throwable) {
        if (throwable instanceof IOException) {
            memberEventLoop.isReady = false;
            do {
                try {
                    Thread.sleep(2000);
                    LOGGER.info("第{}次尝试重连,{}", errorTimes, LocalTime.now().withNano(0));
                    // TODO: 2017/10/13 这里写得怪怪的，先偷懒了
                    NettyClient.getNettyClientByConfig(this);
                } catch (InterruptedException e) {
                    LOGGER.error("thread error, {}.", e);
                }
            } while (!memberEventLoop.isReady && errorTimes++ < 20);

            // 这个判断似乎没啥用
            if (!memberEventLoop.isReady) {
                LOGGER.info("暂时无法重新连接服务器,请重启,{}", LocalTime.now().withNano(0));
            }
        }
    }

}
