package com.framework.akka_router.cluster.node;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.framework.akka_router.ApiRequestForwardEntity;
import com.framework.message.ApiRequestForward;
import com.framework.message.BroadcastMessage;
import com.framework.message.NotifyMessage;
import com.framework.message.WorldMessage;
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

    private ActorSelection clusterRouterFrontend;

    private ActorRef localRouterFrontend;

    private ActorSystem system;

    public static final Timeout DEFAULT_TIMEOUT = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    public Future<Object> askForward(Internal.EnumLite forwardId, ApiRequestForward requestForward) {
        return askForward(forwardId, requestForward, DEFAULT_TIMEOUT);
    }

    public Future<Object> askForward(Internal.EnumLite forwardId, ApiRequestForward requestForward, Timeout timeout) {
        return Patterns.ask(clusterRouterFrontend, ApiRequestForwardEntity.newRequest(forwardId, requestForward), timeout);
    }

    public Future<Object> notify(BroadcastMessage message) {
        return notify(message, DEFAULT_TIMEOUT);
    }

    public Future<Object> notify(BroadcastMessage message, Timeout timeout) {
        return Patterns.ask(clusterRouterFrontend, message, timeout);
    }

    public Future<Object> notify(NotifyMessage message) {
        return notify(message, DEFAULT_TIMEOUT);
    }

    public Future<Object> notify(NotifyMessage message, Timeout timeout) {
        return Patterns.ask(clusterRouterFrontend, message, timeout);
    }

    public Future<Object> notify(WorldMessage message) {
        return notify(message, DEFAULT_TIMEOUT);
    }

    public Future<Object> notify(WorldMessage message, Timeout timeout) {
        return Patterns.ask(clusterRouterFrontend, message, timeout);
    }

    void registerRouterFronted(ActorSelection routerFronted) {
        clusterRouterFrontend = routerFronted;
    }

    void removeRouterFronted() {
        clusterRouterFrontend = null;
    }

    void create() {
        system = ActorSystem.create("AkkaClusterWorkSystem", ConfigFactory.load());
    }

    void create(String config) {
        system = ActorSystem.create("AkkaClusterWorkSystem", ConfigFactory.load(config));
    }

    void registerRouterFronted(Props props) {
        if (localRouterFrontend != null) {
            throw new InvalidParameterException();
        }
        localRouterFrontend = system.actorOf(props, "localRouterFrontend");
    }

}