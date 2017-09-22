package com.game.gateway.portal;

import akka.actor.ActorRef;
import com.framework.akka.MemberOfflineEvent;
import com.framework.akka.MemberOnlineEvent;
import com.framework.niosocket.akka.AbstractMemberEventActorManger;
import com.framework.niosocket.akka.ActorNotifyRemoteHandler;
import com.game.gateway.AkkaRemoteActorEntity;
import com.game.gateway.proto.DOMAIN;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by @panyao on 2017/9/7.
 */
@Component
@Deprecated
public class MemberEventManger extends AbstractMemberEventActorManger {

    @Resource
    private AkkaRemoteActorEntity akkaRemoteActorEntity;

    private ActorRef matching;
    private ActorRef room;

    @PostConstruct
    public void init() {
        matching = super.system.actorOf(ActorNotifyRemoteHandler.props(DOMAIN.MATCHING_VALUE, akkaRemoteActorEntity.getMatchingNotifyPath()));
        room = super.system.actorOf(ActorNotifyRemoteHandler.props(DOMAIN.GAME_ROOM_VALUE, akkaRemoteActorEntity.getRoomNotifyPath()));
    }

    @Override
    protected void onlineEvent(MemberOnlineEvent event) {
        // 分表发送给 远程
//        matching.tell(gateway, null);
//        room.tell(gateway, null);
//        room.tell("", ActorRef.noSender());
    }

    @Override
    protected void offlineEvent(MemberOfflineEvent event) {
        // 分表发送给 远程
//        matching.tell(gateway, null);
//        room.tell(gateway, null);
    }

}
