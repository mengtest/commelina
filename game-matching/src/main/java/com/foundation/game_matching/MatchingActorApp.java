package com.foundation.game_matching;

import akka.actor.ActorSystem;
import com.foundation.game_matching.portal.MatchingRouter;
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
        system.actorOf(MatchingRouter.props(configEntity), "matchingRouter");
    }

}