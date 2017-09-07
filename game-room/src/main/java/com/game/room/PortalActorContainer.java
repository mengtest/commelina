package com.game.room;

import akka.actor.ActorRef;

/**
 * Created by @panyao on 2017/9/7.
 */
public class PortalActorContainer {

    private static PortalActorContainer ourInstance = new PortalActorContainer();

    public static PortalActorContainer getInstance() {
        return ourInstance;
    }

    ActorRef roomRequestActor;
    ActorRef roomServerRequestActor;
    ActorRef roomNotifyActor;

    private PortalActorContainer() {
    }

    public ActorRef getRoomRequestActor() {
        return roomRequestActor;
    }

    public ActorRef getRoomServerRequestActor() {
        return roomServerRequestActor;
    }

    public ActorRef getRoomNotifyActor() {
        return roomNotifyActor;
    }

}
