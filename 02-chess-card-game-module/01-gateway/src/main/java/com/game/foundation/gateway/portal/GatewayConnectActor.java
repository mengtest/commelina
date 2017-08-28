package com.game.foundation.gateway.portal;


import akka.actor.ActorRef;
import com.game.foundation.gateway.MessageProvider;
import com.game.gateway.proto.apis.GATEWAY_APIS;
import com.game.gateway.proto.constants.DOMAIN_CONSTANTS;
import com.game.gateway.proto.constants.OPCODE_CONSTANTS;
import com.google.common.base.Splitter;
import com.google.common.io.BaseEncoding;
import com.nexus.maven.core.message.ApiRequest;
import com.nexus.maven.core.message.RequestArg;
import com.nexus.maven.core.message.ResponseMessage;
import com.nexus.maven.netty.socket.ActorOutputContext;
import com.nexus.maven.netty.socket.ActorWithApiController;
import com.nexus.maven.netty.socket.ActorWithApiHandler;
import com.nexus.maven.netty.socket.ContextAdapter;

import java.util.List;

/**
 * Created by @panyao on 2017/8/25.
 */
@ActorWithApiController(apiName = "" + GATEWAY_APIS.GATEWAY_PASSPORT_CONNECT_V1_0_0_VALUE)
public class GatewayConnectActor implements ActorWithApiHandler {

    @Override
    public int getDomain() {
        return DOMAIN_CONSTANTS.GATE_WAY_VALUE;
    }

    @Override
    public RequestEvent getRouterEvent() {
        return (ApiRequest request, ActorOutputContext context, ActorRef sender) -> {
            RequestArg tokenArg = request.getArg(0);
            if (tokenArg == null) {
                // FIXME: 2017/8/25 null 处理
            }
            String token = tokenArg.getAsString();
            String parseToken = new String(BaseEncoding.base64Url().decode(token));
            List<String> tokenChars = Splitter.on('|').splitToList(parseToken);
            ContextAdapter.userLogin(context.getRawContext().channel().id(), Long.valueOf(tokenChars.get(0)));

            // 回复自己完成了操作
            sender.tell(ResponseMessage.newMessage(MessageProvider.newMessage(OPCODE_CONSTANTS.PASSPORT_CONNECT_VALUE)), sender);
        };
    }
}
