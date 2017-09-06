package com.game.room;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.game.room.portal.RoomClientRouter;
import com.game.room.portal.RoomServerRouter;
import com.game.room.service.RoomManger;
import com.typesafe.config.ConfigFactory;

import javax.annotation.PostConstruct;

/**
 * Created by @panyao on 2017/9/6.
 */
public class RoomActorApp {

    @PostConstruct
    public void init() {
        ActorSystem system = ActorSystem.create("RoomWorkerSystem",
                ConfigFactory.load(("room")));

        ActorRef roomManger = system.actorOf(RoomManger.props(), "roomManger");

        system.actorOf(RoomClientRouter.props(roomManger), "roomClientRouter");

        system.actorOf(RoomServerRouter.props(roomManger), "roomServerRouter");

    }

}
