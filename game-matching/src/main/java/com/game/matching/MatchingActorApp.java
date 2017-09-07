package com.game.matching;

import akka.actor.ActorSystem;
import com.game.matching.portal.MatchingReceiveRequestActor;
import com.typesafe.config.ConfigFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by @panyao on 2017/9/1.
 */
public class MatchingActorApp {

    @Resource
    private MatchingConfigEntity configEntity;

    @PostConstruct
    public void init() {
        ActorSystem system = ActorSystem.create("MatchingWorkerSystem",
                ConfigFactory.load(("matching")));
        system.actorOf(MatchingReceiveRequestActor.props(configEntity), "matchingRouter");
    }

}