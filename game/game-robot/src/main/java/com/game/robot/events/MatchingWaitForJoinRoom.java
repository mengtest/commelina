package com.game.robot.events;

import com.business.game.message.common.proto.DOMAIN;
import com.game.matching.proto.MATCHING_METHODS;
import com.game.robot.interfaces.MemberEventLoop;
import com.game.robot.interfaces.ReadEvent;
import com.github.freedompy.commelina.niosocket.proto.SocketMessage;
import com.google.protobuf.Internal;
/**
 *
 * @author @panyao
 * @date 2017/9/11
 */
public class MatchingWaitForJoinRoom implements ReadEvent {

    @Override
    public Internal.EnumLite getDomain() {
        return DOMAIN.MATCHING;
    }

    @Override
    public Internal.EnumLite getApiOpcode() {
        return MATCHING_METHODS.JOIN_MATCH_QUENE;
    }

    @Override
    public EventResult read(MemberEventLoop eventLoop, SocketMessage msg) {
        // 移除 接受匹配状态的 event
        eventLoop.removeReadEvent(MatchingWaitForMatchStatus.class);

        return null;
    }

}
