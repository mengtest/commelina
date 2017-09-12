package com.game.robot.events;

import com.framework.message.BusinessMessage;
import com.framework.niosocket.proto.SocketMessage;
import com.framework.utils.Generator;
import com.game.gateway.proto.DOMAIN;
import com.game.robot.interfaces.MemberEventLoop;
import com.game.robot.interfaces.ReadEvent;
import com.google.protobuf.Internal;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;


/**
 * Created by @panyao on 2017/9/11.
 */
public class MatchingWaitForMatchStatus implements ReadEvent {

    private final Logger logger = LoggerFactory.getLogger(MatchingWaitForMatchStatus.class);

    @Override
    public boolean isMe(Internal.EnumLite domain, Internal.EnumLite apiOpcode) {
        return domain.getNumber() == DOMAIN.MATCHING_VALUE && apiOpcode.getNumber() == com.game.matching.proto.OPCODE.MATCH_STATUS_VALUE;
    }

    @Override
    public EventResult read(MemberEventLoop eventLoop, ChannelHandlerContext context, SocketMessage msg) {
        try {
            BusinessMessage<Map<String, Integer>> message = Generator.getJsonHolder().readValue(msg.getMsg().toString(), BusinessMessage.class);
            logger.info("当前匹配人数:" + message.getData().get("matchUserCount"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EventResult.UN_REMOVE;
    }

}
