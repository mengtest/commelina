package com.framework.akka_router.cluster;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.framework.akka_router.ApiRequestForwardEntity;
import com.framework.akka_router.RouterRegistrationEntity;
import com.framework.message.ApiRequestForward;
import com.google.protobuf.Internal;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.security.InvalidParameterException;
import java.util.concurrent.TimeUnit;

/**
 * Created by @panyao on 2017/9/25.
 */
public class ClusterChildNodeSystem {

    public static final ClusterChildNodeSystem INSTANCE = new ClusterChildNodeSystem();

    private ActorSelection clusterRouterFronted;

    private ActorRef localRouterFronted;

    private ActorSystem system;

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

    public void create() {
        system = ActorSystem.create("AkkaClusterWorkSystem", ConfigFactory.load());
    }

    public void create(String config) {
        system = ActorSystem.create("AkkaClusterWorkSystem", ConfigFactory.load(config));
    }

    void localRouterRegister(RouterRegistrationEntity routerRegistration, ActorRef actorRef) {
        localRouterFronted.tell(routerRegistration, actorRef);
    }

    void registerRouterFronted(Props props) {
        if (localRouterFronted != null) {
            throw new InvalidParameterException();
        }
        localRouterFronted = system.actorOf(props, "localRouterFronted");
    }

}