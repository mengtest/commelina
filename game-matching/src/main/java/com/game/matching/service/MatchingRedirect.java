package com.game.matching.service;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.framework.akka.router.cluster.nodes.ClusterChildNodeSystem;
import com.framework.akka.router.proto.ApiRequestForward;
import com.framework.core.AppVersion;
import com.google.protobuf.ByteString;
import com.message.matching_room.proto.MATCHING_ROOM_METHODS;

/**
 *
 * 匹配重定向到房间的操作
 *
 * @author @panyao
 * @date 2017/8/14
 */
public class MatchingRedirect extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

//    @Override
//    public void postStop() throws Exception {
//        log.info("MatchingRedirect Application stopped");
//    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CREATE_ROOM.class, this::createRoom)
                .matchAny(o -> log.info("MatchingRedirect received unknown message " + o))
                .build();
    }

    private void createRoom(CREATE_ROOM createRoom) {

        ApiRequestForward.Builder builder = ApiRequestForward.newBuilder()
                .setForward(MATCHING_ROOM_METHODS.CREATE_ROOM_VALUE)
                .setVersion(AppVersion.FIRST_VERSION);

        for (Long userId : createRoom.userIds) {
            builder.addArgs(ByteString.copyFromUtf8(userId.toString()));
        }

        Object result = ClusterChildNodeSystem.INSTANCE.askForward(builder.build());

        if (result == null) {
            // 失败了就把元素投递回去 Matching 队列
            getSender().tell(new CREATE_ROOM_FAILED(createRoom.userIds), getSelf());
        } else {
            // 成功了就关闭此次的重定向 actor
            getContext().stop(getSelf());
        }

    }

    static final class CREATE_ROOM {
        long[] userIds;

        CREATE_ROOM(long[] userIds) {
            this.userIds = userIds;
        }
    }

    static final class CREATE_ROOM_FAILED {
        private long[] userIds;

        CREATE_ROOM_FAILED(long[] userIds) {
            this.userIds = userIds;
        }

        long[] getUserIds() {
            return userIds;
        }
    }

    static Props props() {
        return Props.create(MatchingRedirect.class);
    }

}
