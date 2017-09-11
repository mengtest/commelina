package com.game.robot.events;

import com.framework.niosocket.proto.SocketMessage;
import com.game.gateway.proto.DOMAIN;
import com.game.robot.interfaces.MemberEventLoop;
import com.game.robot.interfaces.ReadEvent;
import com.google.protobuf.Internal;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by @panyao on 2017/9/11.
 */
public class MatchingWaitForMatchStatus implements ReadEvent {

    private Logger logger = LoggerFactory.getLogger(MatchingWaitForMatchStatus.class);

    @Override
    public boolean isMe(Internal.EnumLite domain, Internal.EnumLite apiOpcode) {
        return domain.getNumber() == DOMAIN.MATCHING_VALUE && apiOpcode.getNumber() == com.game.matching.proto.OPCODE.MATCH_STATUS_VALUE;
    }

    @Override
    public EventResult read(MemberEventLoop eventLoop, ChannelHandlerContext context, SocketMessage msg) {

        logger.info("当前匹配人数:" + 9);

        return null;
    }

}
