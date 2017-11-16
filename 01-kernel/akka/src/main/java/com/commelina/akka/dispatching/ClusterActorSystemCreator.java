package com.commelina.akka.dispatching;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;
import com.commelina.akka.dispatching.nodes.AbstractBackendActor;
import com.commelina.akka.dispatching.nodes.ClusterBackendActorSystem;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * @author panyao
 * @date 2017/11/15
 */
public class ClusterActorSystemCreator {

    private static final Timeout DEFAULT_TIMEOUT = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    public static ActorSystem create(String name, String config) {
        return ActorSystem.create(name, ConfigFactory.load(config)
                .withFallback(ConfigFactory.load("default-message-bindings.conf")));
    }

    public static ClusterFrontendActorSystem createAsClusterFrontend(String name, String config) {
        return createAsClusterFrontend(name, config, DEFAULT_TIMEOUT, NioSocketClusterFrontendActor.class);
    }

    public static ClusterFrontendActorSystem createAsClusterFrontend(String name, String config, Timeout timeout, Class<? extends AbstractClusterFrontendActor> frontend) {
        ActorSystem actorSystem = create(name, config);
        return new ClusterFrontendActorSystem(create(name, config), actorSystem.actorOf(Props.create(frontend), Constants.CLUSTER_FRONTEND), timeout);
    }

    public static ClusterBackendActorSystem createAsClusterBackend(String name, String config, Class<? extends AbstractBackendActor> backend) {
        return createAsClusterBackend(name, config, backend, DEFAULT_TIMEOUT);
    }

    public static ClusterBackendActorSystem createAsClusterBackend(String name, String config, Class<? extends AbstractBackendActor> backend, Timeout timeout) {
        ActorSystem actorSystem = create(name, config);
        return new ClusterBackendActorSystem(actorSystem, actorSystem.actorOf(Props.create(backend)), timeout);
    }

}
