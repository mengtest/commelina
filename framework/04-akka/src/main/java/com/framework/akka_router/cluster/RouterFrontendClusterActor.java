package com.framework.akka_router.cluster;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.framework.akka_router.ApiRequestForwardEntity;
import com.framework.akka_router.Rewrite;
import com.framework.akka_router.RouterRegistrationEntity;
import com.framework.akka_router.ServerRequestHandler;
import com.framework.akka_router.local.AkkaLocalWorkerSystem;
import com.framework.message.*;
import com.framework.niosocket.MessageAdapter;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.protobuf.Internal;
import scala.concurrent.Future;

/**
 * Created by @panyao on 2017/9/25.
 */
public class RouterFrontendClusterActor extends AbstractActor implements ServerRequestHandler, Rewrite {

    private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);

    private final BiMap<Internal.EnumLite, ActorRef> clusterNodeRouters = HashBiMap.create(8);

    private final Internal.EnumLite myRouterId;

    public RouterFrontendClusterActor(Internal.EnumLite myRouterId) {
        this.myRouterId = myRouterId;
    }

    @Override
    public final Receive createReceive() {
        return receiveBuilder()
                // 客户端请求
                .match(ApiRequest.class, r -> {
                    ActorRef target = clusterNodeRouters.get(selectActorSeed(r));
                    if (target != null) {
                        // 重定向到远程的 seed node 上，它自己再做 router
                        target.forward(r, getContext());
                    } else {
                        unhandled(r);
                    }
                })
                // from cluster seed node.
                .match(BroadcastMessage.class, b -> {
                    // 获取 router id
                    MessageAdapter.addBroadcast(myRouterId, b);
                })
                .match(NotifyMessage.class, n -> {
                    // 获取 router id
                    MessageAdapter.addNotify(myRouterId, n);
                })
                .match(WorldMessage.class, w -> {
                    // 获取 router id
                    MessageAdapter.addWorld(myRouterId, w);
                })
                // server 请求 重定向， 如 matching -> room
                .match(ApiRequestForwardEntity.class, f -> {
                    ActorRef target = clusterNodeRouters.get(selectActorSeed(f));
                    if (target != null) {
                        // 重定向到远程的 seed node 上，它自己再做 router
                        onForward(f, target);
                    } else {
                        unhandled(f);
                    }
                })
                .match(RouterRegistrationEntity.class, r -> {
                    getContext().watch(sender());
                    clusterNodeRouters.put(r.getRouterId(), sender());
                })
                .match(Terminated.class, terminated -> clusterNodeRouters.inverse().remove(terminated.getActor()))
                .build();
    }

    @Override
    public Internal.EnumLite selectActorSeed(ApiRequest apiRequest) {
        return DEFAULT;
    }

    @Override
    public Internal.EnumLite selectActorSeed(ApiRequestForwardEntity requestForward) {
        return DEFAULT;
    }

    private static final Internal.EnumLite DEFAULT = () -> 0;

    @Override
    public void onForward(ApiRequestForwardEntity request, ActorRef target) {
        Future<Object> future = ClusterAskUtils.askRouterClusterNodeForward(request.getForwardId(), request.getRequestForward());
        // TODO: 2017/9/30 待确定
        // actor 处理成功
        future.onSuccess(new OnSuccess<Object>() {
            @Override
            public void onSuccess(Object result) throws Throwable {

            }
        }, AkkaLocalWorkerSystem.INSTANCE.getSystem().dispatcher());

        future.onFailure(new OnFailure() {
            @Override
            public void onFailure(Throwable failure) throws Throwable {

                logger.error("actor return error.{}", failure);
            }
        }, AkkaLocalWorkerSystem.INSTANCE.getSystem().dispatcher());
    }

}