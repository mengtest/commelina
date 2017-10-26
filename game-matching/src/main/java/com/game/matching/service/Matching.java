package com.game.matching.service;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.github.freedompy.commelina.akka.dispatching.cluster.nodes.AbstractServiceActor;
import com.github.freedompy.commelina.core.DefaultMessageProvider;
import com.google.common.collect.Lists;
import com.google.protobuf.Internal;

import java.util.List;

/**
 * @author @panyao
 * @date 2017/8/10
 */
public class Matching extends AbstractServiceActor {

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
                .match(JoinMatch.class, this::joinMatch)
                .match(RemoveMatch.class, this::removeMatch)
                .match(CancelMatch.class, this::cancelMatch)
                .match(List.class, this::createMatchFailed)
                .matchAny(o -> log.info("Matching received unknown message" + o))
                .build();
    }

    private void joinMatch(JoinMatch joinMatch) {
        final long userId = joinMatch.userId;
        if (matchList.contains(userId)) {
            log.info("userId exists in queue " + userId + ", ignored.");
            if (joinMatch.apiOpcode != null) {
                // 回复 MatchingReceiveRequestActor 的 调用者成功
//                PortalActorContainer.INSTANCE.getMatchingRequestActor()
//                        .tell(ResponseMessage.newMessage(DefaultMessageProvider.produceMessage()), getSelf());
            }
            return;
        }
        if (log.isDebugEnabled()) {
            log.info("add queue userId " + userId);
        }
        matchList.add(userId);

        if (joinMatch.apiOpcode != null) {
            // 回复 MatchingReceiveRequestActor 的 调用者成功
            response(DefaultMessageProvider.produceMessage());
        }

        if (matchList.size() >= MATCH_SUCCESS_PEOPLE) {
            do {
                List<Long> userIds = Lists.newArrayListWithExpectedSize(MATCH_SUCCESS_PEOPLE);
                for (int i = 0; i < MATCH_SUCCESS_PEOPLE && matchList.iterator().hasNext(); i++) {
                    userIds.add(matchList.iterator().next());
                    matchList.iterator().remove();
                }
                final ActorRef matchingRedirect = getContext().actorOf(MatchingRedirect.props());
                matchingRedirect.tell(userIds, getSelf());
            } while (matchList.size() >= MATCH_SUCCESS_PEOPLE);
        } else {
            List<Long> userIds = Lists.newArrayList();
            userIds.addAll(matchList);
            final ActorRef notifyMatchStatus = getContext().actorOf(MatchingStatus.props());
            notifyMatchStatus.tell(userIds, getSelf());
        }
    }

    private void cancelMatch(CancelMatch cancelMatch) {
        long userId = cancelMatch.userId;

        boolean rs = matchList.remove(userId);

        log.info("cancel queue userId " + userId + ", result " + rs);

        // 回复 MatchingReceiveRequestActor 的 调用者成功
        response(DefaultMessageProvider.produceMessage());
    }

    private void removeMatch(RemoveMatch removeMatch) {
        long userId = removeMatch.userId;

        boolean rs = matchList.remove(userId);

        log.info("remove queue userId " + userId + ", result " + rs);
    }

    private void createMatchFailed(List<Long> userIds) {
        // 把用户重新加入失败队列

        for (long userId : userIds) {
            getSelf().tell(new JoinMatch(userId, null), getSelf());
        }

    }

    /**
     * http://doc.akka.io/docs/akka/current/java/guide/tutorial_3.html
     */
    public static final class JoinMatch {
        long userId;
        Internal.EnumLite apiOpcode;

        public JoinMatch(long userId, Internal.EnumLite apiOpcode) {
            this.userId = userId;
            this.apiOpcode = apiOpcode;
        }
    }

    public static final class CancelMatch {
        long userId;
        Internal.EnumLite apiOpcode;

        public CancelMatch(long userId, Internal.EnumLite apiOpcode) {
            this.userId = userId;
            this.apiOpcode = apiOpcode;
        }
    }

    public static final class RemoveMatch {
        long userId;

        public RemoveMatch(long userId) {
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
