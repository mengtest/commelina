package com.module.foundation.game.matching.portal;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.module.foundation.game.matching.methods.MATCHING_METHODS;
import com.module.foundation.game.matching.service.Matching;
import com.nexus.maven.core.akka.RouterActor;
import com.nexus.maven.core.message.ApiRequestWithActor;
import com.nexus.maven.core.message.MemberOfflineEvent;

/**
 * Created by @panyao on 2017/8/29.
 */
public class MatchingRouter extends RouterActor {

    private final ActorRef matching = getContext().actorOf(Matching.props(), "matching");

    @Override
    public boolean onRequest(ApiRequestWithActor request) {
        switch (request.getApiOpcode().getNumber()) {
            case MATCHING_METHODS.JOIN_MATCH_QUENE_VALUE:
                matching.tell(new Matching.JOIN_MATCH(request.getUserId(), request.getApiOpcode()), this.getSelf());
                return true;
            case MATCHING_METHODS.CANCEL_MATCH_QUENE_VALUE:
                matching.tell(new Matching.CANCEL_MATCH(request.getUserId(), request.getApiOpcode()), this.getSelf());
                return true;
        }
        return false;
    }

    @Override
    public void onOffline(MemberOfflineEvent offlineEvent) {
        // 把用户从匹配队列里面移除
        matching.tell(new Matching.REMOVE_MATCH(offlineEvent.getUserId()), this.getSelf());
    }

    public static Props props() {
        return Props.create(MatchingRouter.class);
    }

}
