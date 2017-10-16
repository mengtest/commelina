package com.game.matching.router_v3;

import akka.actor.ActorRef;
import com.framework.akka_router.cluster.node.ClusterChildNodeBackedActor;
import com.framework.message.ApiRequest;
import com.game.matching.service.Matching;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/26.
 */
public class MatchingRouter extends ClusterChildNodeBackedActor {

    private final ActorRef matching = getContext().getSystem().actorOf(Matching.props(10, 2));

    @Override
    public Internal.EnumLite getRouterId() {
        // 这里就是配置节点，节点 1 节点 2 节点 3，不会因为集群的启动顺序而改变
        return () -> 0;
    }

    @Override
    public void onOffline(long logoutUserId) {
        // 用户下线，取消匹配
//        matching.forward(new Matching.CANCEL_MATCH(logoutUserId, () -> 0), getContext());
    }

    @Override
    public void onRequest(ApiRequest request) {
//        switch (request.getOpcode().getNumber()) {
//            // 加入匹配队列
//            case MATCHING_METHODS.JOIN_MATCH_QUENE_VALUE:
//                matching.forward(new Matching.JOIN_MATCH(request.getUserId(), request.getOpcode()), getContext());
//                break;
//            // 用户取消匹配
//            case MATCHING_METHODS.CANCEL_MATCH_QUENE_VALUE:
//                matching.forward(new Matching.JOIN_MATCH(request.getUserId(), request.getOpcode()), getContext());
//                break;
//        }
    }

}