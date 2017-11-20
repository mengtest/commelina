package com.commelina.math24.play.robot.events;

import com.commelina.math24.common.proto.DOMAIN;
import com.commelina.math24.play.match.proto.NOTIFY_OPCODE;
import com.commelina.math24.play.robot.interfaces.MemberEventLoop;
import com.commelina.math24.play.robot.interfaces.ReadEvent;
import com.commelina.niosocket.proto.SocketMessage;
import com.google.protobuf.Internal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author @panyao
 * @date 2017/9/11
 */
public class MatchingWaitForMatchStatus implements ReadEvent {

    private final Logger logger = LoggerFactory.getLogger(MatchingWaitForMatchStatus.class);

    @Override
    public Internal.EnumLite getDomain() {
        return DOMAIN.MATCHING;
    }

    @Override
    public Internal.EnumLite getApiOpcode() {
        return NOTIFY_OPCODE.NOTIFY_MATCH_SUCCESS;
    }

    @Override
    public EventResult read(MemberEventLoop eventLoop, SocketMessage msg) {

        return EventResult.UN_REMOVE;
    }

}
