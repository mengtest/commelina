package com.game.gateway.portal;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.framework.akka.MemberOfflineEvent;
import com.framework.niosocket.ActorNotifyRemoteHandler;
import com.framework.niosocket.ActorSocketMemberEvent;
import com.game.gateway.AkkaRemoteActorEntity;
import com.game.gateway.proto.DOMAIN;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by @panyao on 2017/9/7.
 */
@Component
public class MemberEventManger implements ActorSocketMemberEvent {

    private final ActorSystem system = ActorSystem.create("akkaNotifyContext");

    @Resource
    private AkkaRemoteActorEntity akkaRemoteActorEntity;

    private ActorRef matching;
    private ActorRef room;

    @PostConstruct
    public void init() {
        matching = system.actorOf(ActorNotifyRemoteHandler.props(DOMAIN.MATCHING_VALUE, akkaRemoteActorEntity.getMatchingPath()));
        room = system.actorOf(ActorNotifyRemoteHandler.props(DOMAIN.GAME_ROOM_VALUE, akkaRemoteActorEntity.getRoomPath()));
    }

    @Override
    public void onOnlineEvent(SocketMemberOnlineEvent onlineEvent) {
//        matching.tell(onlineEvent, null);
//        room.tell(onlineEvent, null);
    }

    @Override
    public void onOfflineEvent(SocketMemberOfflineEvent offlineEvent) {
        // 离线事件
        final long userId = offlineEvent.getUserId();
        MemberOfflineEvent event = new MemberOfflineEvent();
        event.setUserId(userId);

        // 分表发送给 远程
        matching.tell(offlineEvent, null);
        room.tell(offlineEvent, null);
    }

}
