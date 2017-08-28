package com.nexus.maven.netty.socket;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.nexus.maven.core.message.ApiRequest;

/**
 * Created by @panyao on 2017/8/25.
 */
public interface ActorWithApiHandler {

    int getDomain();

    default ActorWithApiHandler.OnlineEvent getOnlineEnvent() {
        return null;
    }

    default ActorWithApiHandler.OfflineEvent getOfflineEnvent() {
        return null;
    }

    interface OnlineEvent {
        void onOnline(ActorOutputContext context, AbstractActor actor);
    }

    interface OfflineEvent {
        void onOffline(long userId, ActorOutputContext context, AbstractActor actor);
    }


    RequestEvent getRouterEvent();

    interface RequestEvent {
        void onRequest(ApiRequest request, ActorOutputContext context, ActorRef sender);
    }

}
