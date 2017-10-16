package com.framework.akka_router.cluster;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.pattern.PipeToSupport;
import akka.util.Timeout;
import com.framework.akka_router.ResponseEntity;
import com.framework.message.ApiRequest;
import com.framework.message.ApiRequestForward;
import com.framework.message.MessageBus;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;

import java.security.InvalidParameterException;
import java.util.concurrent.CompletableFuture;
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

    public MessageBus askRouterClusterNode(final ApiRequest apiRequest) {
        return askRouterClusterNode(apiRequest, DEFAULT_TIMEOUT);
    }

    public MessageBus askRouterClusterNode(final ApiRequest apiRequest, Timeout timeout) {
        return (MessageBus) PatternsCS.ask(clusterRouterFrontend, apiRequest, timeout).toCompletableFuture().join();
    }

    PipeToSupport.PipeableCompletionStage<ResponseEntity> askRouterClusterNodeForward(final ApiRequestForward requestForward, ActorRef targetActor) {
        return askRouterClusterNodeFroward(requestForward, targetActor, DEFAULT_TIMEOUT);
    }

    /**
     * https://doc.akka.io/docs/akka/current/java/actors.html#ask-send-and-receive-future
     *
     * @param requestForward
     * @param targetActor
     * @param timeout
     * @return
     */
    PipeToSupport.PipeableCompletionStage<ResponseEntity> askRouterClusterNodeFroward(final ApiRequestForward requestForward, ActorRef targetActor, Timeout timeout) {
        CompletableFuture<Object> askFuture = PatternsCS.ask(clusterRouterFrontend, requestForward, timeout).toCompletableFuture();

        CompletableFuture<ResponseEntity> transformed = CompletableFuture.allOf(askFuture).thenApply(v -> (ResponseEntity) askFuture.join());

        return PatternsCS.pipe(transformed, getSystem().dispatcher()).to(targetActor);
    }

    AkkaMultiWorkerSystem() {

    }

    void create(String clusterName, String config) {
        system = ActorSystem.create(clusterName, ConfigFactory.load().withFallback(ConfigFactory.load(config)));
    }

    void registerRouterFronted(Props props) {
        if (clusterRouterFrontend != null) {
            throw new InvalidParameterException();
        }
        clusterRouterFrontend = system.actorOf(props, "clusterRouterFrontend");
    }

}