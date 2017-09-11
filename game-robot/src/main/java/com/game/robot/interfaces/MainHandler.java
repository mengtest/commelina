package com.game.robot.interfaces;

import com.framework.niosocket.proto.SocketMessage;
import com.game.robot.niosocket.NettyClient;
import com.google.common.collect.Lists;
import io.netty.channel.ChannelHandlerContext;

import java.util.Collections;
import java.util.List;

/**
 * Created by @panyao on 2017/9/11.
 */
public final class MainHandler implements MainGameEvent {

    private final MemberEventLoop memberEventLoop = new MemberEventLoop();

    private List<OutputEvent> initOutputEvents;

    @Override
    public void channelRead(ChannelHandlerContext ctx, SocketMessage msg) {
        memberEventLoop.executeRequest(ctx, msg);
    }

    @Override
    public void connectSuccess(ChannelHandlerContext ctx) {
        memberEventLoop.context = ctx;
        // 执行启动事件
        for (OutputEvent initOutputEvent : initOutputEvents) {
            memberEventLoop.executeMemberEvent(initOutputEvent);
        }
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx) {
        // 优雅关机
        memberEventLoop.eventLoop.shutdownGracefully();
    }

    @Override
    public void heartError(ChannelHandlerContext ctx) {
        disconnect(ctx);
    }

    @Override
    public void start(OutputEvent event, OutputEvent... events) {
        List<OutputEvent> outputEvents = Lists.newArrayList();
        outputEvents.add(event);
        Collections.addAll(outputEvents, events);
        start(outputEvents);
    }

    @Override
    public void start(List<OutputEvent> events) {
        initOutputEvents = events;
        // 根据账号获取 token
        NettyClient.getNettyClient(this);
    }

}
