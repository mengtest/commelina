package com.framework.akka_router.local;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.framework.akka_router.RouterJoinEntity;
import com.framework.akka_router.RouterRegistrationEntity;
import com.framework.message.ApiRequest;
import com.google.protobuf.Internal;
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
    private ActorRef localRouterFronted;

    public static final Timeout defaultTimeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    public Future<Object> askLocalRouterNode(Internal.EnumLite routerId, ApiRequest apiRequest) {
        return askLocalRouterNode(routerId, apiRequest, defaultTimeout);
    }

    public Future<Object> askLocalRouterNode(Internal.EnumLite routerId, ApiRequest apiRequest, Timeout timeout) {
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

    void localRouterRegister(RouterRegistrationEntity routerRegistration, ActorRef actorRef) {
        system.actorSelection("/user/localRouterFronted").tell(routerRegistration, actorRef);
    }

}