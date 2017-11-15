package com.commelina.akka.dispatching;

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

    public static ActorSystem createAsCluster(String name, String config) {
        ActorSystem actorSystem = create(name, config);

        actorSystem.actorOf(Props.create(RouterClusterFrontendActor.class), "frontend");

        return actorSystem;
    }

}
