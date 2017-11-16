package com.commelina.akka.dispatching.nodes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import com.commelina.akka.dispatching.proto.ApiRequestForward;
import com.commelina.niosocket.message.BroadcastMessage;
import com.commelina.niosocket.message.NotifyMessage;
import com.commelina.niosocket.message.WorldMessage;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * @author @panyao
 * @date 2017/9/25
 */
public class ClusterChildNodeSystem {

    public static final ClusterChildNodeSystem INSTANCE = new ClusterChildNodeSystem();

//    private final Logger logger = LoggerFactory.getLogger(ClusterChildNodeSystem.class);

    private ActorRef clusterRouterFrontend;

    private ActorSystem system;

    public static final Timeout DEFAULT_TIMEOUT = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    public Object askForward(ApiRequestForward requestForward) {
        return askForward(requestForward, DEFAULT_TIMEOUT);
    }

    public Object askForward(ApiRequestForward requestForward, Timeout timeout) {
        return PatternsCS.ask(clusterRouterFrontend, requestForward, timeout).toCompletableFuture().join();
    }

    public Object broadcast(BroadcastMessage messageBody) {
        return broadcast(messageBody, DEFAULT_TIMEOUT);
    }

    public Object broadcast(BroadcastMessage message, Timeout timeout) {
        return PatternsCS.ask(clusterRouterFrontend, message, timeout).toCompletableFuture().join();
    }

    public Object notify(NotifyMessage messageBody) {
        return notify(messageBody, DEFAULT_TIMEOUT);
    }

    public Object notify(NotifyMessage messageBody, Timeout timeout) {
        return PatternsCS.ask(clusterRouterFrontend, messageBody, timeout).toCompletableFuture().join();
    }

    public Object world(WorldMessage messageBody) {
        return world(messageBody, DEFAULT_TIMEOUT);
    }

    public Object world(WorldMessage messageBody, Timeout timeout) {
        return PatternsCS.ask(clusterRouterFrontend, messageBody, timeout).toCompletableFuture().join();
    }

    void registerRouterFronted(ActorRef routerFronted) {
        clusterRouterFrontend = routerFronted;
    }

    void create(String clusterName, String config) {
        system = ActorSystem.create(clusterName, ConfigFactory.load(config)
                .withFallback(ConfigFactory.load("default-message-bindings")));
    }

}