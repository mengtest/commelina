package com.business.game.matching;

import akka.actor.ActorRef;
import akka.routing.Routee;
import akka.routing.RoutingLogic;
import com.github.freedompy.commelina.akka.dispatching.cluster.nodes.BackendActor;
import com.github.freedompy.commelina.akka.dispatching.proto.ApiRequest;
import com.business.game.matching.service.Matching;
import scala.collection.immutable.IndexedSeq;

/**
 *
 * @author @panyao
 * @date 2017/9/26
 */
public class MatchingPortal extends BackendActor {

    private final ActorRef matching = getContext().getSystem().actorOf(Matching.props(10, 2));

    @Override
    public void onOffline(long logoutUserId) {
        // 用户下线，取消匹配
//        matching.forward(new Matching.CancelMatch(logoutUserId, () -> 0), getContext());
    }

    @Override
    public void onRequest(ApiRequest request) {
//        switch (request.getOpcode().getNumber()) {
//            // 加入匹配队列
//            case MATCHING_METHODS.JOIN_MATCH_QUENE_VALUE:
//                matching.forward(new Matching.JoinMatch(request.getUserId(), request.getOpcode()), getContext());
//                break;
//            // 用户取消匹配
//            case MATCHING_METHODS.CANCEL_MATCH_QUENE_VALUE:
//                matching.forward(new Matching.JoinMatch(request.getUserId(), request.getOpcode()), getContext());
//                break;
//        }

        new RoutingLogic(){

            @Override
            public Routee select(Object message, IndexedSeq<Routee> routees) {

                return null;
            }

        };
    }

}