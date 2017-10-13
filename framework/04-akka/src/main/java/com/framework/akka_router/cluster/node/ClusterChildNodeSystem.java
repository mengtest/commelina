package com.framework.akka_router.cluster.node;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import com.framework.akka_router.ApiRequestForwardEntity;
import com.framework.message.BroadcastMessage;
import com.framework.message.NotifyMessage;
import com.framework.message.WorldMessage;
import com.google.protobuf.Internal;
import com.typesafe.config.ConfigFactory;
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

    public Object askForward(Internal.EnumLite forwardId, com.framework.message.ApiRequestForward requestForward) {
        return askForward(forwardId, requestForward, DEFAULT_TIMEOUT);
    }

    public Object askForward(Internal.EnumLite forwardId, com.framework.message.ApiRequestForward requestForward, Timeout timeout) {
        return PatternsCS.ask(
                clusterRouterFrontend,
                ApiRequestForwardEntity.newRequest(forwardId, requestForward), timeout)
                .toCompletableFuture()
                .join();
    }

    public Object notify(BroadcastMessage message) {
        return notify(message, DEFAULT_TIMEOUT);
    }

    public Object notify(BroadcastMessage message, Timeout timeout) {
        return PatternsCS.ask(clusterRouterFrontend, message, timeout).toCompletableFuture().join();
    }

    public Object notify(NotifyMessage message) {
        return notify(message, DEFAULT_TIMEOUT);
    }

    public Object notify(NotifyMessage message, Timeout timeout) {
        return PatternsCS.ask(clusterRouterFrontend, message, timeout).toCompletableFuture().join();
    }

    public Object notify(WorldMessage message) {
        return notify(message, DEFAULT_TIMEOUT);
    }

    public Object notify(WorldMessage message, Timeout timeout) {
        return PatternsCS.ask(clusterRouterFrontend, message, timeout).toCompletableFuture().join();
    }

    void registerRouterFronted(ActorSelection routerFronted) {
        clusterRouterFrontend = routerFronted;
    }

    void removeRouterFronted() {
        clusterRouterFrontend = null;
    }

    void create(String clusterName, String config) {
        system = ActorSystem.create(clusterName, ConfigFactory.load(config));
    }

    void registerRouterFronted(Props props) {
        if (localRouterFrontend != null) {
            throw new InvalidParameterException();
        }
        localRouterFrontend = system.actorOf(props, "localRouterFrontend");
    }

}