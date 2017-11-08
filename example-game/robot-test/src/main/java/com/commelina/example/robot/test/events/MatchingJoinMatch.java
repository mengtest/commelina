package com.commelina.example.robot.test.events;

import com.business.game.message.common.proto.DOMAIN;
import com.game.matching.proto.MATCHING_METHODS;
import com.commelina.example.robot.test.interfaces.MemberEvent;
import com.commelina.example.robot.test.interfaces.MemberEventLoop;
import com.github.freedompy.commelina.niosocket.proto.SocketASK;
import com.github.freedompy.commelina.niosocket.proto.SocketMessage;
import com.google.protobuf.ByteString;
import com.google.protobuf.Internal;
/**
 *
 * @author @panyao
 * @date 2017/9/11
 */
public class MatchingJoinMatch implements MemberEvent {

    private final Long userId;

    MatchingJoinMatch(Long userId) {
        this.userId = userId;
    }

    @Override
    public SocketASK handler(MemberEventLoop eventLoop) {
        return SocketASK.newBuilder()
                .setForward(DOMAIN.MATCHING_VALUE)
                .setOpcode(MATCHING_METHODS.JOIN_MATCH_QUENE_VALUE)
                .setVersion("1.0.0")
                .addArgs(ByteString.copyFromUtf8(userId.toString()))
                .build();
    }

    @Override
    public Internal.EnumLite getDomain() {
        return DOMAIN.GATEWAY;
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
