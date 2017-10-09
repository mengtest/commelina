package com.framework.akka_router.cluster;

import akka.actor.ActorSelection;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.framework.akka_router.ApiRequestForwardEntity;
import com.framework.message.ApiRequestForward;
import com.google.protobuf.Internal;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by @panyao on 2017/9/25.
 */
public class ClusterChildNodeSystem {

    public static final ClusterChildNodeSystem INSTANCE = new ClusterChildNodeSystem();

    private ActorSelection clusterRouterFronted;

    public static final Timeout DEFAULT_TIMEOUT = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    public Future<Object> askForward(Internal.EnumLite forwardId, ApiRequestForward requestForward) {
        return askForward(forwardId, requestForward, DEFAULT_TIMEOUT);
    }

    public Future<Object> askForward(Internal.EnumLite forwardId, ApiRequestForward requestForward, Timeout timeout) {
        return Patterns.ask(clusterRouterFronted, ApiRequestForwardEntity.newRequest(forwardId, requestForward), timeout);
    }

    void registerRouterFronted(ActorSelection routerFronted) {
        clusterRouterFronted = routerFronted;
    }

    void removeRouterFronted() {
        clusterRouterFronted = null;
    }

}