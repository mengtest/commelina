package com.commelina.akka.dispatching;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * @author panyao
 * @date 2017/11/15
 */
public class ActorSystemCreator {

    public static ActorSystem create(String name, String config) {
        return ActorSystem.create(name, ConfigFactory.load(config)
                .withFallback(ConfigFactory.load("default-message-bindings.conf")));
    }

    public static ClusterSystem createAsCluster(String name, String config) {
        ActorSystem actorSystem = create(name, config);
        ClusterSystem system = new ClusterSystem();
        system.actorSystem = actorSystem;
        system.frontend = actorSystem.actorOf(Props.create(RouterClusterFrontendActor.class), Constants.CLUSTER_FRONTEND);
        return system;
    }

    public static class ClusterSystem {
        private ActorSystem actorSystem;
        private ActorRef frontend;

        public ActorSystem getActorSystem() {
            return actorSystem;
        }

        public ActorRef getFrontend() {
            return frontend;
        }

    }

}
