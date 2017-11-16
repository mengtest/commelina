package com.commelina.akka.dispatching;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.util.Timeout;

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
}
