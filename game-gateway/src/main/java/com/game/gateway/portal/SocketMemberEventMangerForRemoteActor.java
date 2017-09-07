package com.game.gateway.portal;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.framework.niosocket.ActorSocketMemberEvent;
import com.framework.niosocket.ActorSocketRemoteNotifyHandler;
import com.game.gateway.AkkaRemoteActorEntity;
import com.game.gateway.proto.DOMAIN;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by @panyao on 2017/9/7.
 */
@Component
public class SocketMemberEventMangerForRemoteActor implements ActorSocketMemberEvent {

    private final ActorSystem system = ActorSystem.create("akkaNotifyContext");

    @Resource
    private AkkaRemoteActorEntity akkaRemoteActorEntity;

    private ActorRef matching;
    private ActorRef room;

    @PostConstruct
    public void init() {
        matching = system.actorOf(ActorSocketRemoteNotifyHandler.props(DOMAIN.MATCHING_VALUE, akkaRemoteActorEntity.getMatchingPath()));
        room = system.actorOf(ActorSocketRemoteNotifyHandler.props(DOMAIN.GAME_ROOM_VALUE, akkaRemoteActorEntity.getRoomPath()));
    }

    @Override
    public void onOnlineEvent(SocketMemberOnlineEvent onlineEvent) {
        matching.tell(onlineEvent, null);
        room.tell(onlineEvent, null);
    }

    @Override
    public void onOfflineEvent(SocketMemberOfflineEvent offlineEvent) {
        matching.tell(offlineEvent, null);
        room.tell(offlineEvent, null);
    }

}
