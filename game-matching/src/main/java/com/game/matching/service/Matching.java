package com.game.matching.service;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.framework.message.ResponseMessage;
import com.game.matching.MessageProvider;
import com.game.matching.PortalActorContainer;
import com.google.common.collect.Lists;
import com.google.protobuf.Internal;

import java.util.List;

/**
 * Created by @panyao on 2017/8/10.
 */
public class Matching extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private final List<Long> matchList;
    private final int MATCH_SUCCESS_PEOPLE;

    public Matching(int successPeople, int queueRate) {
        MATCH_SUCCESS_PEOPLE = successPeople;
        matchList = Lists.newArrayListWithExpectedSize(MATCH_SUCCESS_PEOPLE * queueRate);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(JOIN_MATCH.class, this::joinMatch)
                .match(REMOVE_MATCH.class, this::removeMatch)
                .match(CANCEL_MATCH.class, this::cancelMatch)
                .match(MatchingRedirect.CREATE_ROOM_FAILED.class, this::createMatchFailed)
                .matchAny(o -> log.info("Matching received unknown message" + o))
                .build();
    }

    private void joinMatch(JOIN_MATCH joinMatch) {
        final long userId = joinMatch.userId;
        if (matchList.contains(userId)) {
            log.info("userId exists in queue " + userId + ", ignored.");
            if (joinMatch.apiOpcode != null) {
                // 回复 MatchingReceiveRequestActor 的 调用者成功
                getSender().tell(ResponseMessage.newMessage(joinMatch.apiOpcode, MessageProvider.produceMessage()), getSelf());
            }
            return;
        }
        log.info("add queue userId " + userId);
        matchList.add(userId);

        if (joinMatch.apiOpcode != null) {
            // 回复 MatchingReceiveRequestActor 的 调用者成功
            getSender().tell(ResponseMessage.newMessage(joinMatch.apiOpcode, MessageProvider.produceMessage()), getSelf());
        }

        if (matchList.size() >= MATCH_SUCCESS_PEOPLE) {
            do {
                final long[] userIds = new long[MATCH_SUCCESS_PEOPLE];
                for (int i = 0; i < MATCH_SUCCESS_PEOPLE && matchList.iterator().hasNext(); i++) {
                    userIds[i] = matchList.iterator().next();
                    matchList.iterator().remove();
                }
                final ActorRef matchingRedirect = getContext().actorOf(MatchingRedirect.props());
                matchingRedirect.forward(new MatchingRedirect.CREATE_ROOM(userIds), getContext());
            } while (matchList.size() >= MATCH_SUCCESS_PEOPLE);
        } else {
            long[] userIds = new long[matchList.size()];
            for (int i = 0; i < matchList.size(); i++) {
                userIds[i] = matchList.get(i);
            }
            final ActorRef notifyMatchStatus = getContext().actorOf(MatchingStatus.props());
            notifyMatchStatus.forward(new MatchingStatus.NOTIFY_MATCH_STATUS(userIds), getContext());
        }
    }

    private void cancelMatch(CANCEL_MATCH cancelMatch) {
        long userId = cancelMatch.userId;

        boolean rs = matchList.remove(userId);

        log.info("cancel queue userId " + userId + ", result " + rs);

        // 回复 MatchingReceiveRequestActor 的 调用者成功
        getSender().tell(ResponseMessage.newMessage(cancelMatch.apiOpcode, MessageProvider.produceMessage()), getSelf());
    }

    private void removeMatch(REMOVE_MATCH removeMatch) {
        long userId = removeMatch.userId;

        boolean rs = matchList.remove(userId);

        log.info("remove queue userId " + userId + ", result " + rs);
    }

    private void createMatchFailed(MatchingRedirect.CREATE_ROOM_FAILED failed) {
        // 把用户重新加入失败队列
        for (long userId : failed.getUserIds()) {
            getSelf().tell(new JOIN_MATCH(userId, null), getSelf());
        }
    }

    // http://doc.akka.io/docs/akka/current/java/guide/tutorial_3.html
    public static final class JOIN_MATCH {
        long userId;
        Internal.EnumLite apiOpcode;

        public JOIN_MATCH(long userId, Internal.EnumLite apiOpcode) {
            this.userId = userId;
            this.apiOpcode = apiOpcode;
        }
    }

    public static final class CANCEL_MATCH {
        long userId;
        Internal.EnumLite apiOpcode;

        public CANCEL_MATCH(long userId, Internal.EnumLite apiOpcode) {
            this.userId = userId;
            this.apiOpcode = apiOpcode;
        }
    }

    public static final class REMOVE_MATCH {
        long userId;

        public REMOVE_MATCH(long userId) {
            this.userId = userId;
        }
    }

    public static Props props(int successPeople, int queueRate) {
        return Props.create(Matching.class, successPeople, queueRate);
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
