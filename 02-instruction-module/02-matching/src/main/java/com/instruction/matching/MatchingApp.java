package com.instruction.matching;

import akka.actor.ActorSystem;
import com.instruction.matching.portal.MatchingRouter;
import com.typesafe.config.ConfigFactory;

/**
 * Created by @panyao on 2017/8/23.
 */
public class MatchingApp {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("MatchingWorkerSystem",
                ConfigFactory.load(("matching")));

        system.actorOf(MatchingRouter.props(), "matchingRouter");
    }

}
