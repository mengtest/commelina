package com.game.robot.events;

import com.framework.niosocket.proto.SocketASK;
import com.framework.niosocket.proto.SocketMessage;
import com.game.common.proto.DOMAIN;
import com.game.gateway.proto.GATEWAY_APIS;
import com.game.matching.proto.MATCHING_METHODS;
import com.game.robot.interfaces.MemberEvent;
import com.game.robot.interfaces.MemberEventLoop;
import com.google.protobuf.ByteString;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/11.
 */
public class MatchingJoinMatch implements MemberEvent {

    private final Long userId;

    MatchingJoinMatch(Long userId) {
        this.userId = userId;
    }

    @Override
    public SocketASK handler(MemberEventLoop eventLoop) {
        return SocketASK.newBuilder()
                .setForward(GATEWAY_APIS.MATCHING_V1_0_0_VALUE)
                .setOpcode(MATCHING_METHODS.JOIN_MATCH_QUENE_VALUE)
                .setVersion("1.0.0")
                .addArgs(ByteString.copyFromUtf8(userId.toString()))
                .build();
    }

    @Override
    public Internal.EnumLite getDomain() {
        return DOMAIN.GATE_WAY;
    }

    @Override
    public Internal.EnumLite getApiOpcode() {
        return MATCHING_METHODS.JOIN_MATCH_QUENE;
    }

    @Override
    public EventResult read(MemberEventLoop eventLoop, SocketMessage msg) {
        // 加入匹配成功
        // 注册监听匹配状态的事件
        eventLoop.addEvent(new MatchingWaitForMatchStatus());
        // 注册加入匹配的事件
        eventLoop.addEvent(new MatchingWaitForJoinRoom());

        return null;
    }

}
