package com.commelina.math24.play.match.mode;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.commelina.akka.dispatching.proto.MemberOfflineEvent;
import com.commelina.math24.play.match.AbstractMatchServiceActor;
import com.commelina.math24.play.match.proto.JoinMatch;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 全局匹配
 *
 * @author panyao
 * @date 2017/11/10
 */
public class GlobalMatch extends AbstractMatchServiceActor {

    private final int successPeople;

    /**
     * 匹配列表
     */
    private final List<Long> matchList;

    public GlobalMatch(int successPeople) {
        this.successPeople = successPeople;
        this.matchList = Lists.newArrayListWithExpectedSize(successPeople * 3);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(JoinMatch.class, this::join)
                .match(MemberOfflineEvent.class, offlineEvent -> matchList.remove(offlineEvent.getLogoutUserId()))
                .matchEquals(CHECK_MATHC_LIST, c -> checkMatchList())
                .build();
    }

    private void join(JoinMatch match) {
        if (getLogger().isDebugEnabled()) {
            getLogger().info("add queue userId " + match.getUserId());
        }

        matchList.add(match.getUserId());

        checkMatchList();
    }

    private void checkMatchList() {
        if (matchList.size() >= successPeople) {
            do {
                List<Long> userIds = Lists.newArrayListWithExpectedSize(successPeople);
                for (int i = 0; i < successPeople && matchList.iterator().hasNext(); i++) {
                    userIds.add(matchList.iterator().next());
                    matchList.iterator().remove();
                }
                selectRoomManger().tell(userIds, getSelf());
            } while (matchList.size() >= successPeople);
        } else {
            List<Long> userIds = Lists.newArrayList();
            userIds.addAll(matchList);
            final ActorRef notifyMatchStatus = getContext().actorOf(GlobalMatchStatus.props());
            notifyMatchStatus.tell(userIds, getSelf());
        }
    }

    public static Props props(int successPeople) {
        return Props.create(GlobalMatch.class, successPeople);
    }

    /**
     * 检查匹配列表
     */
    static final String CHECK_MATHC_LIST = "check";
}
