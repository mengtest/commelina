package com.commelina.math24.play.match.mode;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.commelina.akka.dispatching.AbstractServiceActor;
import com.commelina.core.DefaultMessageProvider;
import com.commelina.math24.play.match.proto.JoinMatch;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 全局匹配
 *
 * @author panyao
 * @date 2017/11/10
 */
public class GlobalMatch extends AbstractServiceActor {

    private final int successPeople;

    private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);

    /**
     * 匹配列表
     */
    private final List<Long> matchList;

    /**
     * 房间管理 actor
     */
    private ActorRef roomMangerActor;

    public GlobalMatch(int successPeople, ActorRef roomMangerActor) {
        this.successPeople = successPeople;
        this.roomMangerActor = roomMangerActor;
        this.matchList = Lists.newArrayListWithExpectedSize(successPeople * 3);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(JoinMatch.class, this::join)
                .build();
    }

    private void join(JoinMatch joinMatch) {
        if (logger.isDebugEnabled()) {
            logger.info("add queue userId " + joinMatch.getUserId());
        }

        matchList.add(joinMatch.getUserId());

        // 回复客户端成功
        response(DefaultMessageProvider.produceEmptyMessage());

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
                roomMangerActor.tell(userIds, getSelf());
            } while (matchList.size() >= successPeople);
        } else {
            List<Long> userIds = Lists.newArrayList();
            userIds.addAll(matchList);
            final ActorRef notifyMatchStatus = getContext().actorOf(GlobalMatchStatus.props());
            notifyMatchStatus.tell(userIds, getSelf());
        }
    }

    public static Props props(int successPeople, ActorRef roomMangerActor) {
        return Props.create(GlobalMatch.class, successPeople, roomMangerActor);
    }

}
