package com.game.matching;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.game.matching.portal.MatchingReceiveNotifyActor;
import com.game.matching.portal.MatchingReceiveRequestActor;
import com.typesafe.config.ConfigFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by @panyao on 2017/9/1.
 */
@Component
public final class MatchingActorApp {

    @Resource
    private MatchingConfigEntity configEntity;

    @PostConstruct
    public void init() {
        ActorSystem system = ActorSystem.create("MatchingWorkerSystem",
                ConfigFactory.load(("matching")));

        ActorRef matchingRequestRouter =
                system.actorOf(MatchingReceiveRequestActor.props(configEntity), "matchingRequestActor");

        ActorRef matchingNotifyRouter =
                system.actorOf(MatchingReceiveNotifyActor.props(), "matchingNotifyActor");

        PortalActorContainer.INSTANCE.matchingRequestActor = matchingRequestRouter;
        PortalActorContainer.INSTANCE.matchingNotifyActor = matchingNotifyRouter;
    }

}