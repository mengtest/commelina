package com.game.gateway.portal;


import akka.actor.Props;
import com.framework.niosocket.ActorWithApiController;
import com.framework.niosocket.ActorWithApiHandler;
import com.framework.niosocket.ActorWithRequestRouter;
import com.game.gateway.MessageProvider;
import com.game.gateway.proto.DOMAIN;
import com.game.gateway.proto.ERROR_CODE;
import com.game.gateway.proto.GATEWAY_APIS;
import com.framework.message.ApiRouterRequest;
import com.framework.message.BusinessMessage;
import com.framework.message.RequestArg;
import com.framework.message.ResponseMessage;
import com.framework.niosocket.*;
import com.google.common.base.Splitter;
import com.google.common.io.BaseEncoding;

import java.util.List;

/**
 * Created by @panyao on 2017/8/25.
 */
@ActorWithApiController(apiPathCode = GATEWAY_APIS.GATEWAY_V1_0_0_VALUE)
public class GatewayRouterActor implements ActorWithApiHandler {

    public Props getProps(ChannelOutputHandler outputHandler) {
        return GatewayActor.props(GatewayActor.class, DOMAIN.GATE_WAY_VALUE, outputHandler);
    }

    private static class GatewayActor extends ActorWithRequestRouter {

        public GatewayActor(int domain, ChannelOutputHandler context) {
            super(domain, context);
        }

        @Override
        public void onRequest(ApiRouterRequest request) {
            switch (request.getApiOpcode().getNumber()) {
                case 0:
                    RequestArg tokenArg = request.getArg(0);
                    if (tokenArg == null) {
                        // token 转换错误
                        getSelf().tell(ResponseMessage.newMessage(request.getApiOpcode(),
                                MessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.TOKEN_PARSE_ERROR))
                        ), getSelf());
                    }
                    String token = tokenArg.getAsString();
                    String parseToken = new String(BaseEncoding.base64Url().decode(token));
                    List<String> tokenChars = Splitter.on('|').splitToList(parseToken);
                    ContextAdapter.userLogin(context.getRawContext().channel().id(), Long.valueOf(tokenChars.get(0)));

                    // FIXME: 2017/8/30 登陆成功，返回用户状态，如果是 in game 就走重连机制
                    // 回复自己完成了操作
                    getSelf().tell(ResponseMessage.newMessage(request.getApiOpcode(), MessageProvider.produceMessage()), getSender());
                    return;
            }
            this.unhandled(request);
        }
    }


}
