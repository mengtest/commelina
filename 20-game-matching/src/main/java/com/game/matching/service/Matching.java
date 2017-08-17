package com.game.matching.service;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.game.matching.MessageProvider;
import com.game.matching.OpCodeConstants;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.nexus.maven.akka.AkkaResponse;

import java.util.List;
import java.util.Queue;

/**
 * Created by @panyao on 2017/8/10.
 */
public class Matching extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private final Queue<Long> queue = Queues.newArrayDeque();

    private final List<Long> matchList = Lists.newArrayList();

    private static final int MATCH_SUCCESS_PEOPLE = 100;

    private long redirectEventId = 0;

    private final ActorRef notifyMatchStatus = getContext().actorOf(MatchingStatus.props(), "notifyMatchStatus");

//    @Override
//    public void preStart() throws Exception {
//        for (ActorRef each : getContext().getChildren()) {
//            getContext().unwatch(each);
//            getContext().stop(each);
//        }
//        super.preStart();
//    }
//
//    @Override
//    public void postStop() throws Exception {
//        for (ActorRef each : getContext().getChildren()) {
//            getContext().unwatch(each);
//            getContext().stop(each);
//        }
//        super.postStop();
//    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
//                .match(JOIN_MATCH.class, j -> {
//                    long userId = j.userId;
//                    log.info("add queue userId " + userId);
//                    queue.offer(userId);
//                    int queueSize = queue.size();
//                    if (queueSize >= MATCH_SUCCESS_PEOPLE) {
//                        long[] userIds = new long[MATCH_SUCCESS_PEOPLE];
//                        for (int i = 0; i < MATCH_SUCCESS_PEOPLE; i++) {
//                            userIds[i] = queue.poll();
//                        }
//                        this.addMessageToRedirect(userIds);
//                    } else {
//                        long[] userIds = new long[queueSize];
//                        for (int i = 0; queue.iterator().hasNext(); i++) {
//                            final long nextUserId = queue.iterator().next();
//                            log.info("take user id :" + nextUserId);
//                            userIds[i] = nextUserId;
//                        }
//                        this.addMessageToNotify(userIds);
//                    }
//
//                    // 回复 MatchingRouter 的 调用者成功
//                    getSender().tell(AkkaResponse
//                            .newResponse(MessageProvider.newMessage(OpCodeConstants.JOIN_SUCCESS_RESPONSE)), getSelf());
//                })
                .match(JOIN_MATCH.class, j -> {
                    long userId = j.userId;
                    log.info("add queue userId " + userId);

                    matchList.add(userId);

                    if (matchList.size() >= MATCH_SUCCESS_PEOPLE) {
                        long[] userIds = new long[MATCH_SUCCESS_PEOPLE];
                        for (int i = 0; i < MATCH_SUCCESS_PEOPLE; i++) {
                            userIds[i] = matchList.remove(i);
                        }
                        this.addMessageToRedirect(userIds);
                    } else {
                        long[] userIds = new long[matchList.size()];

                        for (int i = 0; i < matchList.size(); i++) {
                            log.info("take user id :" + matchList.get(i));
                            userIds[i] = matchList.get(i);
                        }

                        this.addMessageToNotify(userIds);
                    }

                    // 回复 MatchingRouter 的 调用者成功
                    getSender().tell(AkkaResponse
                            .newResponse(MessageProvider.newMessage(OpCodeConstants.JOIN_SUCCESS_RESPONSE)), getSelf());
                })
                .match(MatchingRedirect.CREATE_ROOM_FAILED.class, f -> {
                    for (long userId : f.getUserIds()) {
                        queue.offer(userId);
                    }
                })
                .matchAny(o -> log.info("Matching received unknown message" + o))
                .build();
    }

    /**
     * 匹配成功，创建房间
     *
     * @param userIds
     */
    private void addMessageToRedirect(long[] userIds) {
        // 事件id 超过边界则重新从 0 开始
        if (redirectEventId == Long.MAX_VALUE) {
            redirectEventId = 0;
        }
        //  这里作为 matching 的子 actor 被创建，重定向完成，即销毁
        final ActorRef actorRef = getContext().actorOf(MatchingRedirect.props(), "redirectEventId:" + redirectEventId++);

        // 发送一个内部消息到 重定向的 actor
//        actorRef.tell(new MatchingRedirect.CREATE_ROOM(userIds), getSelf());
    }

    /**
     * 队列人数不足，则告诉客户端队列匹配人数
     *
     * @param userIds
     */
    private void addMessageToNotify(long[] userIds) {
//        final ActorRef notifyMatchStatus = getContext().actorOf(MatchingStatus.props(), "notifyMatchStatusEvent");
        notifyMatchStatus.forward(new MatchingStatus.NOTIFY_MATCH_STATUS(userIds), getContext());
    }

    // http://doc.akka.io/docs/akka/current/java/guide/tutorial_3.html
    public static final class JOIN_MATCH {
        long userId;

        public JOIN_MATCH(long userId) {
            this.userId = userId;
        }

    }

    public static Props props() {
        return Props.create(Matching.class);
    }

}
