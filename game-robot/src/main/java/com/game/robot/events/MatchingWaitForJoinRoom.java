package com.game.robot.events;

import com.framework.niosocket.proto.SocketMessage;
import com.game.gateway.proto.DOMAIN;
import com.game.matching.proto.MATCHING_METHODS;
import com.game.robot.interfaces.ReadEvent;
import com.game.robot.interfaces.MemberEventLoop;
import com.google.protobuf.Internal;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by @panyao on 2017/9/11.
 */
public class MatchingWaitForJoinRoom implements ReadEvent {

    @Override
    public boolean isMe(Internal.EnumLite domain, Internal.EnumLite apiOpcode) {
        return domain.getNumber() == DOMAIN.MATCHING_VALUE && apiOpcode.getNumber() == MATCHING_METHODS.JOIN_MATCH_QUENE_VALUE;
    }

    @Override
    public EventResult read(MemberEventLoop eventLoop, ChannelHandlerContext context, SocketMessage msg) {
        // 移除 接受匹配状态的 event
        eventLoop.removeReadEvent(MatchingWaitForMatchStatus.class);

        return null;
    }

}
