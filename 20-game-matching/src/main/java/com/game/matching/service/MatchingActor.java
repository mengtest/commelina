package com.game.matching.service;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.google.common.collect.Queues;

import java.util.Queue;

/**
 * Created by @panyao on 2017/8/10.
 */
public class MatchingActor extends UntypedActor {

    public enum MSG {
        MATCHING_ROUTER, MATCHING_REDIRECT_FAILED
    }

    private final Queue<Long> queue = Queues.newArrayDeque();

    private static final int MATCH_SUCCESS_PEOPLE = 100;

    private long redirectEventId = 0;

    @Override
    public void onReceive(Object o) throws Throwable {
        if (o instanceof Long) {
            long userId = (Long) o;
            queue.offer(userId);
            if (queue.size() >= MATCH_SUCCESS_PEOPLE) {
                long[] userIds = new long[MATCH_SUCCESS_PEOPLE];
                for (int i = 0; i < MATCH_SUCCESS_PEOPLE; i++) {
                    userIds[i] = queue.poll();
                }
                this.addMessageToRedirect(userIds);
            }
            // 回复调用者成功
            getSender().tell(MSG.MATCHING_ROUTER, getSelf());
        } else if (o instanceof long[]) {
            long[] userIds = (long[]) o;
            for (int i = 0; i < userIds.length; i++) {
                queue.offer(userIds[i]);
            }
            // 回复调用者成功
            getSender().tell(MSG.MATCHING_REDIRECT_FAILED, getSelf());
        } else {
            this.unhandled(o);
        }
    }

    private void addMessageToRedirect(long[] userIds) {
        // 事件id 超过边界则重新从 0 开始
        if (redirectEventId == Long.MAX_VALUE) {
            redirectEventId = 0;
        }
        final ActorRef matchingActor = getContext().actorOf(Props.create(MatchingRedirectActor.class), "redirectEventId:" + redirectEventId++);
        // 发送一个内部消息到 重定向的 actor
        matchingActor.tell(userIds, getSelf());
    }

}
