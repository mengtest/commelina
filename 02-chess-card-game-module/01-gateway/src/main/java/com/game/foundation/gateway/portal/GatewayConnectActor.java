package com.game.foundation.gateway.portal;


import com.game.foundation.gateway.MessageProvider;
import com.game.gateway.proto.ConstantsDef;
import com.google.common.base.Splitter;
import com.google.common.io.BaseEncoding;
import com.nexus.maven.core.message.ApiRequest;
import com.nexus.maven.core.message.RequestArg;
import com.nexus.maven.netty.socket.ActorOutputContext;
import com.nexus.maven.netty.socket.ActorWithApiController;
import com.nexus.maven.netty.socket.ActorWithApiHandler;
import com.nexus.maven.netty.socket.ContextAdapter;

import java.util.List;

/**
 * Created by @panyao on 2017/8/25.
 */
@ActorWithApiController(apiName = "/gateway/connect/v1.0.0")
public class GatewayConnectActor implements ActorWithApiHandler {

    @Override
    public int getDomain() {
        return ConstantsDef.DOMAIN_CONSTANTS.GATE_WAY_VALUE;
    }

    @Override
    public RequestEvent getRouterEvent() {
        return (ApiRequest request, ActorOutputContext context) -> {
            RequestArg tokenArg = request.getArg(0);
            if (tokenArg == null) {
                // FIXME: 2017/8/25 null 处理
            }
            String token = tokenArg.getAsString();
            String parseToken = new String(BaseEncoding.base64Url().decode(token));
            List<String> tokenChars = Splitter.on('|').splitToList(parseToken);
            ContextAdapter.userLogin(context.getRawContext().channel().id(), Long.valueOf(tokenChars.get(0)));
            return MessageProvider.newMessage(ConstantsDef.OPCODE_CONSTANTS.PASSPORT_CONNECT_VALUE);
        };
    }
}
