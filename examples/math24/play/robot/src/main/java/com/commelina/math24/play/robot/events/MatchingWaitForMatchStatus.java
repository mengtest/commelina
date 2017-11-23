package com.commelina.math24.play.robot.events;

import com.commelina.math24.common.proto.DOMAIN;
import com.commelina.math24.play.match.proto.NOTIFY_OPCODE;
import com.commelina.math24.play.robot.interfaces.ReadEvent;
import com.commelina.math24.play.robot.niosocket.MemberEventLoop;
import com.commelina.niosocket.proto.SocketMessage;
import com.google.protobuf.Internal;


/**
 * @author @panyao
 * @date 2017/9/11
 */
public class MatchingWaitForMatchStatus implements ReadEvent {

    @Override
    public boolean match(Internal.EnumLite forward, Internal.EnumLite opcode) {
        return forward.getNumber() == DOMAIN.MATCHING_VALUE
                && NOTIFY_OPCODE.NOTIFY_MATCH_SUCCESS.getNumber() == opcode.getNumber();
    }

    @Override
    public EventResult onMessage(MemberEventLoop eventLoop, SocketMessage msg) {

        return EventResult.UN_REMOVE;
    }

}
