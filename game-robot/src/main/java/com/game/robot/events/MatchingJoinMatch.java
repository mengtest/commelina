package com.game.robot.events;

import com.framework.niosocket.proto.Arg;
import com.framework.niosocket.proto.SocketASK;
import com.framework.niosocket.proto.SocketMessage;
import com.game.gateway.proto.DOMAIN;
import com.game.gateway.proto.GATEWAY_APIS;
import com.game.matching.proto.MATCHING_METHODS;
import com.game.robot.interfaces.MemberEvent;
import com.game.robot.interfaces.MemberEventLoop;
import com.google.protobuf.ByteString;
import com.google.protobuf.Internal;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by @panyao on 2017/9/11.
 */
public class MatchingJoinMatch implements MemberEvent {

    private final Long userId;

    MatchingJoinMatch(Long userId) {
        this.userId = userId;
    }

    @Override
    public void handle(MemberEventLoop eventLoop, ChannelHandlerContext ctx) {
        SocketASK ask = SocketASK.newBuilder()
                .setApiCode(GATEWAY_APIS.MATCHING_V1_0_0_VALUE)
                .setApiMethod(MATCHING_METHODS.CANCEL_MATCH_QUENE_VALUE)
                .setVersion("1.0.0")
                .setArgs(0, Arg.newBuilder().setValue(ByteString.copyFrom(new byte[]{userId.byteValue()})))
                .build();
        ctx.writeAndFlush(ask);
    }

    @Override
    public boolean isMe(Internal.EnumLite domain, Internal.EnumLite apiOpcode) {
        return domain.getNumber() == DOMAIN.GATE_WAY_VALUE && apiOpcode.getNumber() == MATCHING_METHODS.JOIN_MATCH_QUENE_VALUE;
    }

    @Override
    public EventResult read(MemberEventLoop eventLoop, ChannelHandlerContext context, SocketMessage msg) {
        // 加入匹配成功
        // 注册监听匹配状态的事件
        eventLoop.addReadEvent(new MatchingWaitForMatchStatus());
        // 注册加入匹配的事件
        eventLoop.addReadEvent(new MatchingWaitForJoinRoom());

        return null;
    }

}
