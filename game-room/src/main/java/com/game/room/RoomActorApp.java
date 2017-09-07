package com.game.room;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.game.room.portal.RoomReceiveNotifyActor;
import com.game.room.portal.RoomReceiveRequestActor;
import com.game.room.portal.RoomReceiveServerRequestActor;
import com.game.room.service.RoomManger;
import com.typesafe.config.ConfigFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by @panyao on 2017/9/6.
 */
@Component
public class RoomActorApp {

    @PostConstruct
    public void init() {
        ActorSystem system = ActorSystem.create("RoomWorkerSystem",
                ConfigFactory.load(("room")));

        ActorRef roomManger = system.actorOf(RoomManger.props(), "roomManger");

        ActorRef roomRequestActor
                = system.actorOf(RoomReceiveRequestActor.props(roomManger), "roomRequestActor");

        ActorRef roomServerRequestActor
                = system.actorOf(RoomReceiveServerRequestActor.props(roomManger), "roomServerRequestActor");

        ActorRef roomNotifyActor
                = system.actorOf(RoomReceiveNotifyActor.props(), "roomNotifyActor");


    }

}
