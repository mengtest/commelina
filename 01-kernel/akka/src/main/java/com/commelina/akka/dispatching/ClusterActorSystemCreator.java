package com.commelina.akka.dispatching;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;
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
        return new ClusterFrontendActorSystem(actorSystem, actorSystem.actorOf(Props.create(frontend), Constants.CLUSTER_FRONTEND), timeout);
    }

}
