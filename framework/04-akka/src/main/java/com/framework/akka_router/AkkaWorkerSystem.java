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

    public final ActorSystem system;
    private final ActorRef routerFronted;

    public final Timeout defaultTimeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    public void localRouterRegister(LocalRouterRegistrationEntity routerRegistration, Props senderProps) {
        system.actorSelection("/user/routerFronted").tell(routerRegistration, system.actorOf(senderProps));
    }

    public Future<Object> routerAsk(ClusterRouterJoinEntity join) {
        return routerAsk(join, defaultTimeout);
    }

    public Future<Object> routerAsk(ClusterRouterJoinEntity join, Timeout timeout) {
        return ask(routerFronted, join, timeout);
    }

    public Future<Object> routerAsk(LocalRouterJoinEntity join) {
        return routerAsk(join, defaultTimeout);
    }

    public Future<Object> routerAsk(LocalRouterJoinEntity join, Timeout timeout) {
        return ask(routerFronted, join, timeout);
    }

    private AkkaWorkerSystem() {
        system = ActorSystem.create("ClusterSystem", (Config) null);
        routerFronted = system.actorOf(Props.create(RouterFrontend.class), "routerFronted");
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}