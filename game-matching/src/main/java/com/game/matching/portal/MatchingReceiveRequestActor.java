package com.game.matching.portal;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.framework.akka.AbstractReceiveRequestActor;
import com.game.matching.service.Matching;
import com.game.matching.MatchingConfigEntity;
import com.game.matching.proto.MATCHING_METHODS;
import com.framework.message.ApiRequest;

/**
 * Created by @panyao on 2017/8/29.
 */
public class MatchingReceiveRequestActor extends AbstractReceiveRequestActor {

    private final ActorRef matching;

    // FIXME: 2017/9/6 这里留着 room 这边处理了再改
    public MatchingReceiveRequestActor(MatchingConfigEntity configEntity) {
        matching = getContext().actorOf(Matching.props(
                configEntity.getQueueSuccessPeople(), configEntity.getQueueSizeRate()), "matching");
    }

    @Override
    public void onRequest(ApiRequest request) {
        switch (request.getOpcode().getNumber()) {
            case MATCHING_METHODS.JOIN_MATCH_QUENE_VALUE:
                matching.tell(new Matching.JOIN_MATCH(request.getUserId(), request.getOpcode()), getSelf());
                return;
            case MATCHING_METHODS.CANCEL_MATCH_QUENE_VALUE:
                matching.tell(new Matching.CANCEL_MATCH(request.getUserId(), request.getOpcode()), getSelf());
                return;
        }
        this.unhandled(request);
    }

//    @Override
//    public void onOffline(MemberOfflineEvent offlineEvent) {
//        // 把用户从匹配队列里面移除
//        matching.tell(new Matching.REMOVE_MATCH(offlineEvent.getUserId()), getSelf());
//    }

    public static Props props(MatchingConfigEntity configEntity) {
        return Props.create(MatchingReceiveRequestActor.class, configEntity);
    }

}
