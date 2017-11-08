package com.github.freedompy.commelina.akkadispatching.cluster;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import com.github.freedompy.commelina.akkadispatching.proto.ApiRequest;
import com.github.freedompy.commelina.core.MessageBody;
import com.google.protobuf.GeneratedMessageV3;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;

import java.security.InvalidParameterException;
import java.util.concurrent.TimeUnit;

/**
 * 工作的线程， 一个独立的 akka system
 *
 * @author @panyao
 * @date 2017/9/25
 */
public class AkkaMultiWorkerSystem {

    private ActorSystem system;

    private ActorRef clusterRouterFrontend;

    public static final Timeout DEFAULT_TIMEOUT = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    public ActorSystem getSystem() {
        return system;
    }

    public Object askRouterClusterNode(final GeneratedMessageV3 ask) {
        return askRouterClusterNode(ask, DEFAULT_TIMEOUT);
    }

    public Object askRouterClusterNode(final GeneratedMessageV3 ask, Timeout timeout) {
        return (MessageBody) PatternsCS.ask(clusterRouterFrontend, ask, timeout).toCompletableFuture().join();
    }

    public MessageBody askRouterClusterNode(final ApiRequest ask) {
        return askRouterClusterNode(ask, DEFAULT_TIMEOUT);
    }

    public MessageBody askRouterClusterNode(final ApiRequest ask, Timeout timeout) {
        return (MessageBody) PatternsCS.ask(clusterRouterFrontend, ask, timeout).toCompletableFuture().join();
    }

    AkkaMultiWorkerSystem() {

    }

    void create(String clusterName, String config) {
        system = ActorSystem.create(clusterName, ConfigFactory.load(config)
                .withFallback(ConfigFactory.load("default-remote-message-bindings.conf")));
    }

    void registerRouterFronted(Props props) {
        if (clusterRouterFrontend != null) {
            throw new InvalidParameterException();
        }
        clusterRouterFrontend = system.actorOf(props, "clusterRouterFrontend");
    }

}