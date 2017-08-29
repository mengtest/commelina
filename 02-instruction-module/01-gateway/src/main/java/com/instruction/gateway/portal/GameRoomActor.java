package com.instruction.gateway.portal;

import com.instruction.gateway.proto.DOMAIN_CONSTANTS;
import com.instruction.gateway.proto.GATEWAY_APIS;
import com.nexus.maven.core.message.AkkaActorApiRequest;
import com.nexus.maven.core.message.RequestArg;
import com.nexus.maven.netty.socket.ActorWithApiController;
import com.nexus.maven.netty.socket.ActorWithApiRemoteHandler;
import com.nexus.maven.netty.socket.ContextAdapter;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by @panyao on 2017/8/25.
 */
@ActorWithApiController(apiName = "" + GATEWAY_APIS.GAME_ROOM_REDIRECT_v1_0_0_VALUE)
public class GameRoomActor implements ActorWithApiRemoteHandler {

    @Value("akka.remote.actor.roomPath:xxxxxxxxxxxxxxxxxxxxxxxxx")
    private String remotePath;

    @Override
    public int getDomain() {
        return DOMAIN_CONSTANTS.GAME_ROOM_VALUE;
    }

    @Override
    public String getRemoteActorPath() {
        return remotePath;
    }

    @Override
    public RequestEvent getRouterEvent() {
        return ((request, context, sender) -> {
            RequestArg redirectPathArg = request.getArg(0);
            if (redirectPathArg == null) {
                // FIXME: 2017/8/25 远程路由地址
            }

            RequestArg roomId = request.getArg(1);
            if (roomId == null) {
                // FIXME: 2017/8/25 必须是持有房间id
            }

            long userId = ContextAdapter.getLoginUserId(context.getRawContext().channel().id());
            if (userId <= 0) {
                // FIXME: 2017/8/25 必须登陆
            }

            String remoteApiPath = redirectPathArg.getAsString();

            RequestArg[] args = new RequestArg[request.getArgs().length - 1];
            for (int i = 1; i < request.getArgs().length; i++) {
                args[i - 1] = request.getArgs()[i];
            }

            sender.tell(new AkkaActorApiRequest(remoteApiPath, request.getVersion(), userId, args), sender);
        });
    }

}
