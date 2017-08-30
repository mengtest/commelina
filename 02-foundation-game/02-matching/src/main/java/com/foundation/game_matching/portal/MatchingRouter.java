package com.foundation.game_matching.portal;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.foundation.game_matching.proto.MATCHING_METHODS;
import com.foundation.game_matching.service.Matching;
import com.framework.core_message.ApiRequestWithActor;
import com.framework.core_message.MemberOfflineEvent;
import com.framework.coreakka.RouterActor;

/**
 * Created by @panyao on 2017/8/29.
 */
public class MatchingRouter extends RouterActor {

    private final ActorRef matching = getContext().actorOf(Matching.props(), "matching");

    @Override
    public boolean onRequest(ApiRequestWithActor request) {
        switch (request.getApiOpcode().getNumber()) {
            case MATCHING_METHODS.JOIN_MATCH_QUENE_VALUE:
                matching.tell(new Matching.JOIN_MATCH(request.getUserId(), request.getApiOpcode()), getSelf());
                return true;
            case MATCHING_METHODS.CANCEL_MATCH_QUENE_VALUE:
                matching.tell(new Matching.CANCEL_MATCH(request.getUserId(), request.getApiOpcode()), getSelf());
                return true;
        }
        return false;
    }

    @Override
    public void onOffline(MemberOfflineEvent offlineEvent) {
        // 把用户从匹配队列里面移除
        matching.tell(new Matching.REMOVE_MATCH(offlineEvent.getUserId()), getSelf());
    }

    public static Props props() {
        return Props.create(MatchingRouter.class);
    }

}