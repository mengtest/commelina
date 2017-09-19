package com.game.matching;

import akka.actor.ActorRef;

/**
 * Created by @panyao on 2017/9/7.
 */
public final class PortalActorContainer {

    public static final PortalActorContainer INSTANCE = new PortalActorContainer();

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

    public PortalActorContainer updateRequestActor(ActorRef matchingRequestActor) {
        this.matchingRequestActor = matchingRequestActor;
        return this;
    }

    public PortalActorContainer updateNotifyActor(ActorRef matchingNotifyActor) {
        this.matchingNotifyActor = matchingNotifyActor;
        return this;
    }

}
