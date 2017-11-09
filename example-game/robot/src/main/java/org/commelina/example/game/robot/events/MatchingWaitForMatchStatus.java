package org.commelina.example.game.robot.events;

import com.business.game.message.common.proto.DOMAIN;
import org.commelina.example.game.robot.interfaces.MemberEventLoop;
import org.commelina.example.game.robot.message.BusinessMessage;
import com.game.matching.proto.OPCODE;
import org.commelina.example.game.robot.interfaces.ReadEvent;
import org.commelina.example.game.robot.utils.Generator;
import com.github.freedompy.commelina.niosocket.proto.SocketMessage;
import com.google.protobuf.Internal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;


/**
 *
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
        return OPCODE.MATCH_STATUS;
    }

    @Override
    public EventResult read(MemberEventLoop eventLoop, SocketMessage msg) {
        try {
            BusinessMessage<Map<String, Integer>> message = Generator.getJsonHolder().readValue(msg.getMsg().toString(), BusinessMessage.class);
            logger.info("当前匹配人数:" + message.getData().get("matchUserCount"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EventResult.UN_REMOVE;
    }

}
