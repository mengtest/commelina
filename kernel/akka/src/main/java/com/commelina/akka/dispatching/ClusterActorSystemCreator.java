package com.commelina.akka.dispatching;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;
import com.commelina.akka.dispatching.nodes.AbstractBackendActor;
import com.commelina.akka.dispatching.nodes.MetricsListener;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

/**
 * @author panyao
 * @date 2017/11/15
 */
public class ClusterActorSystemCreator {

    private static final FiniteDuration DURATION = Duration.create(5, TimeUnit.SECONDS);

    public static ActorSystem create(String name, String config) {
        return ActorSystem.create(name, ConfigFactory.load(config)
                .withFallback(ConfigFactory.load("default-message-bindings.conf")));
    }

    public static ClusterFrontendActorSystem createAsClusterFrontend(String name, String config) {
        return createAsClusterFrontend(name, config, DURATION, NioSocketClusterFrontendActor.class);
    }

    public static ClusterFrontendActorSystem createAsClusterFrontend(String name, String config, FiniteDuration timeout) {
        return createAsClusterFrontend(name, config, timeout, NioSocketClusterFrontendActor.class);
    }

    public static ClusterFrontendActorSystem createAsClusterFrontend(String name, String config,
                                                                     Class<? extends AbstractClusterFrontendActor> frontend) {
        return createAsClusterFrontend(name, config, DURATION, frontend);
    }

    public static ClusterFrontendActorSystem createAsClusterFrontend(String name, String config, FiniteDuration timeout,
                                                                     Class<? extends AbstractClusterFrontendActor> frontend) {
        ActorSystem actorSystem = create(name, config);
        return new ClusterFrontendActorSystem(create(name, config), actorSystem.actorOf(Props.create(frontend), Constants.CLUSTER_FRONTEND), new Timeout(timeout));
    }

    public static ActorSystem createAsClusterBackend(String name, String config, Class<? extends AbstractBackendActor> backend) {
        ActorSystem actorSystem = create(name, config);
        actorSystem.actorOf(Props.create(backend));
        return actorSystem;
    }

    public static ActorSystem createAsClusterBackend(String name, String config, Class<? extends AbstractBackendActor> backend, Class<MetricsListener> metricsListenerClass) {
        ActorSystem actorSystem = createAsClusterBackend(name, config, backend);
        actorSystem.actorOf(Props.create(metricsListenerClass));
        return actorSystem;
    }

}
