package com.framework.akka_router.cluster;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.framework.akka_router.RouterJoinEntity;
import com.framework.akka_router.RouterRegistrationEntity;
import com.framework.message.ApiRequest;
import com.framework.message.ApiRequestForward;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.protobuf.Internal;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.security.InvalidParameterException;
import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;

/**
 * Created by @panyao on 2017/9/25.
 * <p>
 * 工作的线程， 一个独立的 akka system
 */
public class AkkaMultiWorkerSystem {

    public static final AkkaMultiWorkerSystem INSTANCE = new AkkaMultiWorkerSystem();

    private ActorSystem system;
    private ActorRef localRouterFronted;
    private final BiMap<Internal.EnumLite, ActorRef> clusterActors = HashBiMap.create(4);

    public static final Timeout defaultTimeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    public Future<Object> askRouterClusterNode(Internal.EnumLite routerId, ApiRequest apiRequest) {
        return askRouterClusterNode(routerId, apiRequest, defaultTimeout);
    }

    public Future<Object> askRouterClusterNode(Internal.EnumLite routerId, ApiRequest apiRequest, Timeout timeout) {
        return ask(clusterActors.get(routerId), apiRequest, timeout);
    }

    public Future<Object> askRouterClusterNode(ApiRequestForward apiRequest) {
        return askRouterClusterNode(apiRequest, defaultTimeout);
    }

    public Future<Object> askRouterClusterNode(ApiRequestForward apiRequest, Timeout timeout) {
        return ask(clusterActors.get(apiRequest.getForwardId()), apiRequest, timeout);
    }

    public Future<Object> askRouterLocalNode(Internal.EnumLite routerId, ApiRequest apiRequest) {
        return askRouterLocalNode(routerId, apiRequest, defaultTimeout);
    }

    public Future<Object> askRouterLocalNode(Internal.EnumLite routerId, ApiRequest apiRequest, Timeout timeout) {
        return Patterns.ask(localRouterFronted, new RouterJoinEntity(routerId, apiRequest), timeout);
    }

    public ActorSystem getSystem() {
        return system;
    }

    void create(String config) {
        system = ActorSystem.create("AkkaWorkSystem", ConfigFactory.load(config));
    }

    void registerLocal(Props props) {
        if (localRouterFronted != null) {
            throw new InvalidParameterException();
        }
        localRouterFronted = system.actorOf(props, "localRouterFronted");
    }

    void registerCluster(Internal.EnumLite routerId, Props props) {
        clusterActors.put(routerId, system.actorOf(props, "clusterRouterFronted"));
    }

    void localRouterRegister(RouterRegistrationEntity routerRegistration, Props senderProps) {
        system.actorSelection("/user/localRouterFronted").tell(routerRegistration, system.actorOf(senderProps));
    }




}