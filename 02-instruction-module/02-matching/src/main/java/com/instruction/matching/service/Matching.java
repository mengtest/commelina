package com.instruction.matching.service;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.common.collect.Lists;
import com.instruction.matching.MessageProvider;
import com.instruction.matching.constants.ConstantsDef;
import com.nexus.maven.core.message.ResponseMessage;

import java.util.List;

/**
 * Created by @panyao on 2017/8/10.
 */
public class Matching extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private final List<Long> matchList = Lists.newArrayList();
    private static final int MATCH_SUCCESS_PEOPLE = 100;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(JOIN_MATCH.class, this::joinMatch)
                .match(REMOVE_MATCH.class, this::removeMatch)
                .match(MatchingRedirect.CREATE_ROOM_FAILED.class, this::createMatchFailed)
                .matchAny(o -> log.info("Matching received unknown message" + o))
                .build();
    }

    private void joinMatch(JOIN_MATCH joinMatch) {
        long userId = joinMatch.userId;
        log.info("add queue userId " + userId);

        matchList.add(userId);

        // 回复 MatchingRouter 的 调用者成功
        getSender().tell(ResponseMessage.newMessage(MessageProvider.newMessage(ConstantsDef.OPCODE_CONSTANTS.JOIN_MATCH_RESPONSE_VALUE)), getSelf());

        if (matchList.size() >= MATCH_SUCCESS_PEOPLE) {
            final long[] userIds = new long[MATCH_SUCCESS_PEOPLE];
            for (int i = 0; i < MATCH_SUCCESS_PEOPLE; i++) {
                userIds[i] = matchList.remove(i);
            }
            final ActorRef matchingRedirect = getContext().actorOf(MatchingRedirect.props());
            matchingRedirect.forward(new MatchingRedirect.CREATE_ROOM(userIds), getContext());
        } else {
            long[] userIds = new long[matchList.size()];
            for (int i = 0; i < matchList.size(); i++) {
                userIds[i] = matchList.get(i);
            }
            final ActorRef notifyMatchStatus = getContext().actorOf(MatchingStatus.props());
            notifyMatchStatus.forward(new MatchingStatus.NOTIFY_MATCH_STATUS(userIds), getContext());
        }
    }

    private void removeMatch(REMOVE_MATCH remove_match) {
        long userId = remove_match.userId;

        boolean rs = matchList.remove(userId);

        log.info("remove queue userId " + userId + ", result " + rs);

        // 回复 MatchingRouter 的 调用者成功
        getSender().tell(ResponseMessage.newMessage(MessageProvider.newMessage(ConstantsDef.OPCODE_CONSTANTS.REMOTE_MATCH_RESPONSE_VALUE)), getSelf());
    }

    private void createMatchFailed(MatchingRedirect.CREATE_ROOM_FAILED failed) {
        for (long userId : failed.getUserIds()) {
            matchList.add(userId);
        }
    }

    // http://doc.akka.io/docs/akka/current/java/guide/tutorial_3.html
    public static final class JOIN_MATCH {
        long userId;

        public JOIN_MATCH(long userId) {
            this.userId = userId;
        }

    }

    public static final class REMOVE_MATCH {
        long userId;

        public REMOVE_MATCH(long userId) {
            this.userId = userId;
        }

    }

    public static Props props() {
        return Props.create(Matching.class);
    }

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

}
