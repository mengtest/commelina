package com.commelina.akka.dispatching;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import com.commelina.akka.dispatching.proto.ActorResponse;
import com.commelina.akka.dispatching.proto.ApiRequest;

/**
 * @author panyao
 * @date 2017/11/16
 */
public class ClusterFrontendActorSystem {

    private final ActorSystem actorSystem;
    private final ActorRef frontend;
    private final Timeout timeout;

    ClusterFrontendActorSystem(ActorSystem actorSystem, ActorRef frontend, Timeout timeout) {
        this.actorSystem = actorSystem;
        this.frontend = frontend;
        this.timeout = timeout;
    }

    public ActorSystem getActorSystem() {
        return actorSystem;
    }

    public ActorRef getFrontend() {
        return frontend;
    }

    public ActorResponse askForBackend(ApiRequest request) {
        return askForBackend(request, timeout);
    }

    public ActorResponse askForBackend(ApiRequest request, Timeout timeout) {
        return (ActorResponse) PatternsCS.ask(frontend, request, timeout)
                .toCompletableFuture()
                .join();
    }

}
