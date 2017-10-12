package com.framework.akka_router.local;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import com.framework.akka_router.RouterRegistrationEntity;
import com.framework.message.ApiRequest;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.security.InvalidParameterException;
import java.util.concurrent.TimeUnit;

/**
 * Created by @panyao on 2017/9/25.
 * <p>
 * 工作的线程， 一个独立的 akka system
 */
public class AkkaLocalWorkerSystem {

    public static final AkkaLocalWorkerSystem INSTANCE = new AkkaLocalWorkerSystem();

    private ActorSystem system;
    private ActorRef localRouterFrontend;

    public static final Timeout DEFAULT_TIMEOUT = new Timeout(Duration.create(15, TimeUnit.SECONDS));

    public Future<Object> askLocalRouterNode(ApiRequest apiRequest) {

        return askLocalRouterNode(apiRequest, DEFAULT_TIMEOUT);
    }

    public Future<Object> askLocalRouterNode(ApiRequest apiRequest, Timeout timeout) {
        Object object = PatternsCS.ask(localRouterFrontend, apiRequest, timeout).toCompletableFuture().join();
        System.out.println(object);
        return null;
    }

    public ActorSystem getSystem() {
        return system;
    }

    void create() {
        system = ActorSystem.create("AkkaWorkSystem", ConfigFactory.load());
    }

    void create(String config) {
        system = ActorSystem.create("AkkaWorkSystem", ConfigFactory.load(config));
    }

    void registerRouterFronted(Props props) {
        if (localRouterFrontend != null) {
            throw new InvalidParameterException();
        }
        localRouterFrontend = system.actorOf(props, "localRouterFrontend");
    }

    void localRouterRegister(RouterRegistrationEntity routerRegistration, ActorRef actorRef) {
        system.actorSelection("/user/localRouterFrontend").tell(routerRegistration, actorRef);
    }

}