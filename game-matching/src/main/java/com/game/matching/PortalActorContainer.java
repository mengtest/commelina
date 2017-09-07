package com.game.matching;

import akka.actor.ActorRef;

/**
 * Created by @panyao on 2017/9/7.
 */
public class PortalActorContainer {

    private static PortalActorContainer ourInstance = new PortalActorContainer();

    public static PortalActorContainer getInstance() {
        return ourInstance;
    }

    ActorRef matchingRequestActor;
    ActorRef matchingNotifyActor;

    private PortalActorContainer() {
    }

    public ActorRef getMatchingRequestActor() {
        return matchingRequestActor;
    }

    public ActorRef getMatchingNotifyActor() {
        return matchingNotifyActor;
    }
}
