package com.commelina.math24.play.robot.events;

import com.commelina.math24.common.proto.DOMAIN;
import com.commelina.math24.play.match.proto.REQUEST_OPCODE;
import com.commelina.math24.play.robot.interfaces.MemberEvent;
import com.commelina.math24.play.robot.niosocket.MemberEventLoop;
import com.commelina.niosocket.proto.RequestBody;
import com.commelina.niosocket.proto.SocketASK;
import com.commelina.niosocket.proto.SocketMessage;
import com.commelina.utils.Version;
import com.google.protobuf.Internal;

/**
 * @author @panyao
 * @date 2017/9/11
 */
public class MatchingJoinMatch implements MemberEvent {

    @Override
    public SocketASK onCreatedAsk(MemberEventLoop eventLoop) {
        return SocketASK.newBuilder()
                .setForward(DOMAIN.MATCHING_VALUE)
                .setBody(RequestBody.newBuilder()
                        .setOpcode(REQUEST_OPCODE.JOIN_MATCH_QUENE_VALUE)
                        .setVercode(Version.create("1.0.0"))
                )
                .build();
    }

    @Override
    public boolean tag(Internal.EnumLite forward, Internal.EnumLite opcode) {
        return DOMAIN.MATCHING_VALUE == forward.getNumber() && REQUEST_OPCODE.JOIN_MATCH_QUENE.equals(opcode);
    }

    @Override
    public EventResult onResponse(MemberEventLoop eventLoop, SocketMessage msg) {
        // 加入匹配成功
        // 注册监听匹配状态的事件
        eventLoop.addEvent(new MatchingWaitForMatchStatus());
        // 注册加入匹配的事件
        eventLoop.addEvent(new MatchingWaitForJoinRoom());

        return null;
    }

}
