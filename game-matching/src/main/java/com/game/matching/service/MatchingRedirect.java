package com.game.matching.service;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.framework.akka_router.cluster.node.ClusterChildNodeSystem;
import com.framework.core.AppVersion;
import com.framework.message.ApiRequestForward;
import com.framework.message.RequestArg;
import com.game.common.proto.DOMAIN;
import com.message.matching_room.proto.MATCHING_ROOM_METHODS;
import scala.concurrent.Future;

/**
 * Created by @panyao on 2017/8/14.
 * <p>
 * 匹配重定向到房间的操作
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

        Future<Object> result = ClusterChildNodeSystem.INSTANCE.askForward(
                DOMAIN.GAME_ROOM,
                new ApiRequestForward(
                        MATCHING_ROOM_METHODS.CREATE_ROOM,
                        AppVersion.FIRST_VERSION,
                        RequestArg.asList(createRoom.userIds)));

        result.onSuccess(new OnSuccess<Object>() {
            @Override
            public void onSuccess(Object result) throws Throwable {
                // 成功了就关闭此次的重定向 actor
                getContext().stop(getSelf());
            }
        }, getContext().getSystem().dispatcher());

        result.onFailure(new OnFailure() {
            @Override
            public void onFailure(Throwable failure) throws Throwable {
                // 失败了就把元素投递回去 Matching 队列
                getSender().tell(new CREATE_ROOM_FAILED(createRoom.userIds), getSelf());
            }
        }, getContext().getSystem().dispatcher());

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
