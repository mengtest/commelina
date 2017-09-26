package com.framework.akka_router;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;
import com.typesafe.config.Config;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;

/**
 * Created by @panyao on 2017/9/25.
 * <p>
 * 工作的线程， 一个独立的 akka system
 */
public class AkkaWorkerSystem {

    public static final class Holder {
        public static AkkaWorkerSystem AKKA_WORKER_SYSTEM = new AkkaWorkerSystem();
    }

    private final ActorSystem system;
    private final ActorRef localRouterFronted;
    private final ActorRef clusterRouterFronted;

    public static final Timeout defaultTimeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    public void localRouterRegister(LocalRouterRegistrationEntity routerRegistration, Props senderProps) {
        system.actorSelection("/user/localRouterFronted").tell(routerRegistration, system.actorOf(senderProps));
    }

    public Future<Object> routerClusterNodeAsk(ClusterRouterJoinEntity join) {
        return routerClusterNodeAsk(join, defaultTimeout);
    }

    public Future<Object> routerClusterNodeAsk(ClusterRouterJoinEntity join, Timeout timeout) {
        return ask(clusterRouterFronted, join, timeout);
    }

    public Future<Object> routerLocalNodeAsk(LocalRouterJoinEntity join) {
        return routerLocalNodeAsk(join, defaultTimeout);
    }

    public Future<Object> routerLocalNodeAsk(LocalRouterJoinEntity join, Timeout timeout) {
        return ask(localRouterFronted, join, timeout);
    }

    private AkkaWorkerSystem() {
        system = ActorSystem.create("AkkaWorkSystem", (Config) null);

        localRouterFronted = system.actorOf(Props.create(RouterFrontendLocalActor.class), "localRouterFronted");

        clusterRouterFronted = system.actorOf(Props.create(RouterFrontendClusterActor.class), "clusterRouterFronted");
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public ActorSystem getSystem() {
        return system;
    }
}