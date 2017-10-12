package com.framework.akka_router.cluster;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.framework.message.ApiRequest;
import com.framework.message.ApiRequestForward;
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
public class AkkaMultiWorkerSystem {

    private ActorSystem system;

    private ActorRef clusterRouterFrontend;

    public static final Timeout DEFAULT_TIMEOUT = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    public ActorSystem getSystem() {
        return system;
    }

    public Future<Object> askRouterClusterNode(final ApiRequest apiRequest) {
        return askRouterClusterNode(apiRequest, DEFAULT_TIMEOUT);
    }

    public Future<Object> askRouterClusterNode(final ApiRequest apiRequest, Timeout timeout) {
        return Patterns.ask(clusterRouterFrontend, apiRequest, timeout);
    }

    public Future<Object> askRouterClusterNode(final ApiRequestForward requestForward) {
        return askRouterClusterNode(requestForward, DEFAULT_TIMEOUT);
    }

    public Future<Object> askRouterClusterNode(final ApiRequestForward requestForward, Timeout timeout) {
        return Patterns.ask(clusterRouterFrontend, requestForward, timeout);
    }

    public Future<Object> notifyRouterClusterNode(final ApiRequestForward requestForward) {
        return askRouterClusterNode(requestForward, DEFAULT_TIMEOUT);
    }

    public Future<Object> notifyRouterClusterNode(final ApiRequestForward requestForward, Timeout timeout) {
        return Patterns.ask(clusterRouterFrontend, requestForward, timeout);
    }

    AkkaMultiWorkerSystem() {

    }

    void create(String clusterName, String config) {
        system = ActorSystem.create(clusterName, ConfigFactory.load(config));
    }

    void registerRouterFronted(Props props) {
        if (clusterRouterFrontend != null) {
            throw new InvalidParameterException();
        }
        clusterRouterFrontend = system.actorOf(props, "clusterRouterFrontend");
    }

}