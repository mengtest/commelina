package com.game.matching.service;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by @panyao on 2017/8/14.
 * <p>
 * 匹配重定向到房间的操作
 */
public class MatchingRedirect extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);



    @Override
    public void postStop() throws Exception {
        log.info("MatchingRedirect Application stopped");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CREATE_ROOM.class, c -> {
                    // 成功了就关闭此次的重定向 actor
                    if (!this.createRoom(c.userIds)) {
                        // 失败了就把元素投递回去 Matching 队列
                        getSender().tell(new CREATE_ROOM_FAILED(c.userIds), getSelf());
                        // FIXME: 2017/8/14 面临死循环问题
                    }
                    // 失败的重新投递回去，就关闭此次的 actor
//                    getContext().stop(getSelf());
                })
                .build();
    }

    private boolean createRoom(long[] userIds) {

        // 创建定时器 10s 不成功，则认为失败了
//        getContext().system().scheduler().scheduleOnce(Duration.create(10000, TimeUnit.MILLISECONDS), () -> {
//            // 停止当前 actor
//            getContext().stop(getSelf());
//        }, getContext().system().dispatcher());

        // FIXME: 2017/8/15 创建房间的操作
        return false;
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
