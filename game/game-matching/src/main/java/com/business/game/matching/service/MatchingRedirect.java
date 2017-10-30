package com.business.game.matching.service;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.business.game.message.common.proto.DOMAIN;
import com.business.game.message.matching_room.proto.MATCHING_ROOM_METHODS;
import com.github.freedompy.commelina.akka.dispatching.cluster.nodes.ClusterChildNodeSystem;
import com.github.freedompy.commelina.akka.dispatching.proto.ApiRequestForward;
import com.github.freedompy.commelina.core.AppVersion;
import com.google.protobuf.ByteString;

import java.util.List;

/**
 * 匹配重定向到房间的操作
 *
 * @author @panyao
 * @date 2017/8/14
 */
class MatchingRedirect extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

//    @Override
//    public void postStop() throws Exception {
//        log.info("MatchingRedirect Application stopped");
//    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(List.class, this::createRoom)
                .matchAny(o -> log.info("MatchingRedirect received unknown message " + o))
                .build();
    }

    private void createRoom(List<Long> userIds) {

        ApiRequestForward.Builder builder = ApiRequestForward.newBuilder()
                .setForward(DOMAIN.GAME_ROOM_VALUE)
                .setOpcode(MATCHING_ROOM_METHODS.CREATE_ROOM_VALUE)
                .setVersion(AppVersion.FIRST_VERSION);

        for (Long userId : userIds) {
            builder.addArgs(ByteString.copyFromUtf8(userId.toString()));
        }

        Object result = ClusterChildNodeSystem.INSTANCE.askForward(builder.build());

        if (result == null) {
            // 失败了就把元素投递回去 Matching 队列
            getSender().tell(userIds, getSelf());
        } else {
            // 成功了就关闭此次的重定向 actor
            getContext().stop(getSelf());
        }

    }

    static Props props() {
        return Props.create(MatchingRedirect.class);
    }

}
