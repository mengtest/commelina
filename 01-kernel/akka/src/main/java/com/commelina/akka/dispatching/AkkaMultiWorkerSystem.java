package com.commelina.akka.dispatching;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import com.commelina.akka.dispatching.proto.ApiRequest;
import com.commelina.core.MessageBody;
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
@Deprecated
public class AkkaMultiWorkerSystem {

    private ActorSystem system;

    private ActorRef frontend;

    public static final Timeout DEFAULT_TIMEOUT = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    public ActorSystem getActorSystem() {
        return system;
    }

    public MessageBody askActor(final ApiRequest ask) {
        return askActor(ask, DEFAULT_TIMEOUT);
    }

    public MessageBody askActor(final ApiRequest ask, Timeout timeout) {
        return (MessageBody) PatternsCS.ask(frontend, ask, timeout).toCompletableFuture().join();
    }

    AkkaMultiWorkerSystem() {

    }

    void create(String clusterName, String config) {
        system = ActorSystem.create(clusterName, ConfigFactory.load(config)
                .withFallback(ConfigFactory.load("default-message-bindings.conf")));
    }

    void registerRouterFronted(Props props) {
        if (frontend != null) {
            throw new InvalidParameterException();
        }
        frontend = system.actorOf(props, "frontend");
    }

}