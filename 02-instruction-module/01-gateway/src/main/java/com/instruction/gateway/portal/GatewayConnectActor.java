package com.instruction.gateway.portal;


import akka.actor.Props;
import com.google.common.base.Splitter;
import com.google.common.io.BaseEncoding;
import com.instruction.gateway.MessageProvider;
import com.instruction.gateway.proto.DOMAIN_CONSTANTS;
import com.instruction.gateway.proto.GATEWAY_APIS;
import com.instruction.gateway.proto.OPCODE_CONSTANTS;
import com.nexus.maven.core.message.ApiRequest;
import com.nexus.maven.core.message.RequestArg;
import com.nexus.maven.core.message.ResponseMessage;
import com.nexus.maven.netty.socket.*;

import java.util.List;

/**
 * Created by @panyao on 2017/8/25.
 */
@ActorWithApiController(apiName = "" + GATEWAY_APIS.GATEWAY_PASSPORT_CONNECT_V1_0_0_VALUE)
public class GatewayConnectActor implements ActorWithApiHandler {

    @Override
    public Props getProps(ChannelOutputHandler outputHandler) {
        return GatewayActor.props(DOMAIN_CONSTANTS.GATE_WAY_VALUE, outputHandler);
    }

    private static class GatewayActor extends ActorWithRequestRouter {

        public GatewayActor(int domain, ChannelOutputHandler context) {
            super(domain, context);
        }

        @Override
        protected void onRequest(ApiRequest request) {
            RequestArg tokenArg = request.getArg(0);
            if (tokenArg == null) {
                // FIXME: 2017/8/25 null 处理
            }
            String token = tokenArg.getAsString();
            String parseToken = new String(BaseEncoding.base64Url().decode(token));
            List<String> tokenChars = Splitter.on('|').splitToList(parseToken);
            ContextAdapter.userLogin(context.getRawContext().channel().id(), Long.valueOf(tokenChars.get(0)));

            // 回复自己完成了操作
            getSelf().tell(ResponseMessage.newMessage(MessageProvider.newMessage(OPCODE_CONSTANTS.PASSPORT_CONNECT_VALUE)), getSender());
        }
    }

}
