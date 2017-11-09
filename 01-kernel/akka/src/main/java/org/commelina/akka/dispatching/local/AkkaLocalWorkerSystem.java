package org.commelina.akka.dispatching.local;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import org.commelina.akka.dispatching.RouterRegistration;
import org.commelina.akka.dispatching.proto.ApiRequest;
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
public class AkkaLocalWorkerSystem {

    public static final AkkaLocalWorkerSystem INSTANCE = new AkkaLocalWorkerSystem();

    private ActorSystem system;
    private ActorRef localRouterFrontend;

    public static final Timeout DEFAULT_TIMEOUT = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    public Object askLocalRouterNodeWithRaw(Object msg) {
        return askLocalRouterNodeWithRaw(msg, DEFAULT_TIMEOUT);
    }

    public Object askLocalRouterNodeWithRaw(Object msg, Timeout timeout) {
        return PatternsCS.ask(localRouterFrontend, msg, timeout).toCompletableFuture().join();
    }

    public Object askLocalRouterNode(GeneratedMessageV3 messageV3, Timeout timeout) {
        return PatternsCS.ask(localRouterFrontend, messageV3, timeout).toCompletableFuture().join();
    }

    public Object askLocalRouterNode(GeneratedMessageV3 messageV3) {
        return askLocalRouterNode(messageV3, DEFAULT_TIMEOUT);
    }

    public Object askLocalRouterNode(ApiRequest ask) {
        return askLocalRouterNode(ask, DEFAULT_TIMEOUT);
    }

    public Object askLocalRouterNode(ApiRequest ask, Timeout timeout) {
        return PatternsCS.ask(localRouterFrontend, ask, timeout).toCompletableFuture().join();
    }

    public ActorSystem getSystem() {
        return system;
    }

    void create(String config) {
        system = ActorSystem.create("AkkaWorkSystem", ConfigFactory.load().withFallback(ConfigFactory.load(config)));
    }

    void registerRouterFronted(Props props) {
        if (localRouterFrontend != null) {
            throw new InvalidParameterException();
        }
        localRouterFrontend = system.actorOf(props, "localRouterFrontend");
    }

    void localRouterRegister(RouterRegistration routerRegistration, ActorRef actorRef) {
        system.actorSelection("/user/localRouterFrontend").tell(routerRegistration, actorRef);
    }

}