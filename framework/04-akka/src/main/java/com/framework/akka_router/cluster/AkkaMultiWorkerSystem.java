package com.framework.akka_router.cluster;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import com.framework.akka_router.ApiRequest;
import com.framework.core.MessageBody;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;

import java.security.InvalidParameterException;
import java.util.concurrent.TimeUnit;

/**
 * Created by @panyao on 2017/9/25.
 * <p>
 * 工作的线程， 一个独立的 akka system
 */
public class AkkaMultiWorkerSystem {

    private ActorSystem system;

    private ActorRef clusterRouterFrontend;

    public static final Timeout DEFAULT_TIMEOUT = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    public ActorSystem getSystem() {
        return system;
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