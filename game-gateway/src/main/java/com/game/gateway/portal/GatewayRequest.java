package com.game.gateway.portal;


import akka.actor.Props;
import com.framework.message.ApiRequest;
import com.framework.message.BusinessMessage;
import com.framework.message.RequestArg;
import com.framework.message.ResponseMessage;
import com.framework.niosocket.*;
import com.game.gateway.MessageProvider;
import com.game.gateway.proto.DOMAIN;
import com.game.gateway.proto.ERROR_CODE;
import com.game.gateway.proto.GATEWAY_APIS;
import com.game.gateway.proto.GATEWAY_METHODS;

/**
 * Created by @panyao on 2017/8/25.
 */
@ActorRequestController(apiPathCode = GATEWAY_APIS.GATEWAY_V1_0_0_VALUE)
public class GatewayRequest implements ActorRequest {

    public Props getProps(ChannelOutputHandler outputHandler) {
        return GatewayActorRequest.props(GatewayActorRequest.class, DOMAIN.GATE_WAY_VALUE, outputHandler);
    }

    private static class GatewayActorRequest extends ActorRequestHandler {

        public GatewayActorRequest(int domain, ChannelOutputHandler context) {
            super(domain, context);
        }

        @Override
        public void onRequest(ApiRequest request) {
            switch (request.getApiOpcode().getNumber()) {
                case GATEWAY_METHODS.PASPPORT_CONNECT_VALUE:
                    RequestArg tokenArg = request.getArg(0);
                    if (tokenArg == null) {
                        // token 转换错误
                        reply(ResponseMessage.newMessage(request.getApiOpcode(),
                                MessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.TOKEN_PARSE_ERROR))));
                        return;
                    }
//                    String token = tokenArg.getAsString();
//                    String parseToken = new String(BaseEncoding.base64Url().decode(token));
//                    List<String> tokenChars = Splitter.on('|').splitToList(parseToken);
//                    ContextAdapter.userLogin(context.getRawContext().channel().id(), Long.valueOf(tokenChars.get(0)));
                    ContextAdapter.userLogin(context.getRawContext().channel().id(), tokenArg.getAsLong());

                    // FIXME: 2017/8/30 登陆成功，返回用户状态，如果是 in game 就走重连机制
                    // 回复自己完成了操作
                    reply(ResponseMessage.newMessage(request.getApiOpcode(), MessageProvider.produceMessage()));
                    return;
            }
            this.unhandled(request);
        }
    }


}
