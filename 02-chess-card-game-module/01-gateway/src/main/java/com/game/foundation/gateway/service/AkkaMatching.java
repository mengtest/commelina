package com.game.foundation.gateway.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.game.foundation.gateway.service.akka.MatchingRemoteClientActor;
import com.typesafe.config.ConfigFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by @panyao on 2017/8/14.
 */
@Component
public class AkkaMatching {

    @Value("${akka.remote.actor.matchingPath:akka.tcp://MatchingWorkerSystem@127.0.0.1:2551/user/matchingRouter}")
    private String matchingRemotePath;

    @PostConstruct
    public void remoteActorInit() {
        final ActorSystem system = ActorSystem.create("MatchingRouterSystem",
                ConfigFactory.load("matching-local"));
        final ActorRef matchingRouterActor = system.actorOf(
                Props.create(MatchingRemoteClientActor.class, matchingRemotePath), "matchingRouterActor");
    }

}
