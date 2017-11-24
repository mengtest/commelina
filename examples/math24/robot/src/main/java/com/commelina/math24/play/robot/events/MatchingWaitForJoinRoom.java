package com.commelina.math24.play.robot.events;

import com.commelina.math24.common.proto.DOMAIN;
import com.commelina.math24.play.match.proto.REQUEST_OPCODE;
import com.commelina.math24.play.robot.niosocket.MemberEventLoop;
import com.commelina.math24.play.robot.interfaces.ReadEvent;
import com.commelina.niosocket.proto.SocketMessage;
import com.google.protobuf.Internal;
/**
 *
 * @author @panyao
 * @date 2017/9/11
 */
public class MatchingWaitForJoinRoom implements ReadEvent {

    @Override
    public EventResult onMessage(MemberEventLoop eventLoop, SocketMessage msg) {
        // 移除 接受匹配状态的 event
        eventLoop.removeReadEvent(MatchingWaitForMatchStatus.class);

        return null;
    }

    @Override
    public boolean match(Internal.EnumLite forward, Internal.EnumLite opcode) {
        return forward.getNumber() == DOMAIN.MATCHING_VALUE && opcode.getNumber() == REQUEST_OPCODE.JOIN_MATCH_QUENE_VALUE;
    }
}
