package com.instruction.matching.service;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.game.instruction.matching.MessageProvider;
import com.game.instruction.matching.OpCodeConstants;
import com.google.common.collect.Lists;
import com.nexus.maven.core.message.ResponseMessage;

import java.util.List;

/**
 * Created by @panyao on 2017/8/10.
 */
public class Matching extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private final List<Long> matchList = Lists.newArrayList();

    private static final int MATCH_SUCCESS_PEOPLE = 100;

    private long redirectEventId = 0;

    private final ActorRef notifyMatchStatus = getContext().actorOf(com.game.instruction.matching.service.MatchingStatus.props(), "notifyMatchStatus");

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
                .match(JOIN_MATCH.class, this::joinMatch)
                .match(MatchingRedirect.CREATE_ROOM_FAILED.class, this::createMatchFailed)
                .matchAny(o -> log.info("Matching received unknown message" + o))
                .build();
    }

    private void joinMatch(JOIN_MATCH joinMatch) {
        long userId = joinMatch.userId;
        log.info("add queue userId " + userId);

        matchList.add(userId);

        // 回复 MatchingRouter 的 调用者成功
        getSender().tell(ResponseMessage.newMessage(MessageProvider.newMessage(OpCodeConstants.JOIN_SUCCESS_RESPONSE)), getSelf());

        if (matchList.size() >= MATCH_SUCCESS_PEOPLE) {
            final long[] userIds = new long[MATCH_SUCCESS_PEOPLE];
            for (int i = 0; i < MATCH_SUCCESS_PEOPLE; i++) {
                userIds[i] = matchList.remove(i);
            }
            this.addMessageToRedirect(userIds);
        } else {
            long[] userIds = new long[matchList.size()];
            for (int i = 0; i < matchList.size(); i++) {
                userIds[i] = matchList.get(i);
            }
            notifyMatchStatus.forward(new MatchingStatus.NOTIFY_MATCH_STATUS(userIds), getContext());
        }
    }

    private void createMatchFailed(MatchingRedirect.CREATE_ROOM_FAILED failed) {
        for (long userId : failed.getUserIds()) {
            matchList.add(userId);
        }
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
