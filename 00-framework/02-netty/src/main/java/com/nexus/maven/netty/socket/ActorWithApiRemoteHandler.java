package com.nexus.maven.netty.socket;

import akka.actor.AbstractActor;
import com.nexus.maven.core.message.AkkaActorApiRequest;
import com.nexus.maven.core.message.ApiRequest;
import com.nexus.maven.core.message.RequestArg;

/**
 * Created by @panyao on 2017/8/25.
 */
public interface ActorWithApiRemoteHandler extends ActorRouterHandler {

    String getRemoteActorPath();

    RemoteRouterEvent getRouterEvent();

    class RouterHandler {


    }


    RemoteRouterEvent DEFAULT_REMOTE_ROUTER_EVENT = ((request, context, abstractActor) -> {
        RequestArg pathArg = request.getArg(0);
        if (pathArg == null) {
            // FIXME: 2017/8/25 远程路由地址
        }

        long userId = ContextAdapter.getLoginUserId(context.getRawContext().channel().id());
        if (userId <= 0) {
            // FIXME: 2017/8/25 远程路由地址
        }

        String remoteApiPath = pathArg.getAsString();

        RequestArg[] args = new RequestArg[request.getArgs().length - 1];
        for (int i = 1; i < request.getArgs().length; i++) {
            args[i - 1] = request.getArgs()[i];
        }

        return new AkkaActorApiRequest(remoteApiPath, request.getVersion(), userId, args);
    });

    interface RemoteRouterEvent {
        AkkaActorApiRequest onRequest(ApiRequest apiRequest, ActorOutputContext context, AbstractActor abstractActor);
    }
}
