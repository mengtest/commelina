package com.game.instruction.gateway.portal;

import com.game.gateway.proto.apis.GATEWAY_APIS;
import com.game.gateway.proto.constants.DOMAIN_CONSTANTS;
import com.nexus.maven.core.message.AkkaActorApiRequest;
import com.nexus.maven.core.message.RequestArg;
import com.nexus.maven.netty.socket.ActorWithApiController;
import com.nexus.maven.netty.socket.ActorWithApiRemoteHandler;
import com.nexus.maven.netty.socket.ContextAdapter;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by @panyao on 2017/8/25.
 */
@ActorWithApiController(apiName = "" + GATEWAY_APIS.MATCHING_REDIRECT_V1_0_0_VALUE)
public class MatchingActor implements ActorWithApiRemoteHandler {

    @Value("akka.remote.actor.matchingPath:akka.tcp://MatchingWorkerSystem@127.0.0.1:2551/user/matchingRouter")
    private String remotePath;

    @Override
    public int getDomain() {
        return DOMAIN_CONSTANTS.MATCHING_VALUE;
    }

    @Override
    public String getRemoteActorPath() {
        return remotePath;
    }

    @Override
    public RequestEvent getRouterEvent() {
        return ((request, context, sender) -> {
            RequestArg pathArg = request.getArg(0);
            if (pathArg == null) {
                // FIXME: 2017/8/25 远程路由地址
            }

            long userId = ContextAdapter.getLoginUserId(context.getRawContext().channel().id());
            if (userId <= 0) {
                // FIXME: 2017/8/25 必须登陆
            }

            String remoteApiPath = pathArg.getAsString();

            RequestArg[] args = new RequestArg[request.getArgs().length - 1];
            for (int i = 1; i < request.getArgs().length; i++) {
                args[i - 1] = request.getArgs()[i];
            }

            sender.tell(new AkkaActorApiRequest(remoteApiPath, request.getVersion(), userId, args), sender);
        });
    }
}
