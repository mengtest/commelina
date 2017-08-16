package com.game.matching.service;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.game.matching.OpCodeConstants;
import com.game.matching.portal.MatchingGroup;
import com.google.common.collect.Queues;
import com.nexus.maven.akka.AkkaResponse;
import com.nexus.maven.core.message.JsonMessage;

import java.util.Queue;

/**
 * Created by @panyao on 2017/8/10.
 */
public class Matching extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private final Queue<Long> queue = Queues.newArrayDeque();

    private static final int MATCH_SUCCESS_PEOPLE = 100;

    private long redirectEventId = 0;
    private long notifyEventId = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(JOIN_MATCH.class, j -> {
                    long userId = j.userId;
                    queue.offer(userId);
                    int queueSize = queue.size();
                    if (queueSize >= MATCH_SUCCESS_PEOPLE) {
                        long[] userIds = new long[MATCH_SUCCESS_PEOPLE];
                        for (int i = 0; i < MATCH_SUCCESS_PEOPLE; i++) {
                            userIds[i] = queue.poll();
                        }
                        this.addMessageToRedirect(userIds);
                    } else {
                        long[] userIds = new long[queueSize];
                        for (int i = 0; i < queueSize; i++) {
                            userIds[i] = queue.element();
                        }
                        this.addMessageToNotify(userIds);
                    }

                    ActorSelection group = getContext().system().actorSelection(MatchingGroup.GOURP_PATH);
                    // 回复调用者成功
                    group.tell(AkkaResponse
                            .newResponse(JsonMessage.newMessage(OpCodeConstants.JOIN_SUCCESS_RESPONSE)), getSelf());
                })
                .match(MatchingRedirect.CREATE_ROOM_FAILED.class, f -> {
                    final long[] userIds = f.getUserIds();
                    for (int i = 0; i < userIds.length; i++) {
                        queue.offer(userIds[i]);
                    }
                    // 回复调用者成功
                    getSender().tell(new CREATE_ROOM_FAILED_TRY_SUCCESS(), getSelf());
                })
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
        final ActorRef actorRef = getContext().actorOf(MatchingRedirect.props(), "redirectEventId:" + redirectEventId++);
        // 发送一个内部消息到 重定向的 actor
        actorRef.tell(new MatchingRedirect.CREATE_ROOM(userIds), getSelf());
    }

    /**
     * 队列人数不足，则告诉客户端队列匹配人数
     *
     * @param userIds
     */
    private void addMessageToNotify(long[] userIds) {
        // 事件id 超过边界则重新从 0 开始
        if (notifyEventId == Long.MAX_VALUE) {
            notifyEventId = 0;
        }
        final ActorRef actorRef = getContext().actorOf(MatchingStatus.props(), "notifyEventId:" + notifyEventId++);
        actorRef.tell(new MatchingStatus.NOTIFY_MATCH_STATUS(userIds), getSelf());
    }

    // http://doc.akka.io/docs/akka/current/java/guide/tutorial_3.html
    public static final class JOIN_MATCH {
        long userId;

        public JOIN_MATCH(long userId) {
            this.userId = userId;
        }

    }

     static final class CREATE_ROOM_FAILED_TRY_SUCCESS {

    }

    public static Props props() {
        return Props.create(Matching.class);
    }

}
