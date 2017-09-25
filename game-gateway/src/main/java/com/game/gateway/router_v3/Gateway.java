package com.game.gateway.router_v3;

import akka.actor.Props;
import com.framework.akka_router.AbstractServiceActor;
import com.framework.akka_router.NioWokerActor;
import com.framework.message.ApiRequest;
import com.framework.niosocket.NioSocketRouter;
import com.game.gateway.proto.DOMAIN;
import com.game.gateway.proto.GATEWAY_APIS;
import com.game.gateway.service.SessionInterface;
import com.google.protobuf.Internal;

import javax.annotation.Resource;

/**
 * Created by @panyao on 2017/9/22.
 */
@NioSocketRouter(apiPathCode = GATEWAY_APIS.GATEWAY_V1_0_0_VALUE)
public class Gateway extends NioWokerActor {

    @Resource
    private SessionInterface sessionInterface;

    @Override
    protected Internal.EnumLite getDomain() {
        return DOMAIN.GATE_WAY;
    }

    @Override
    protected Props getProps() {
        return GateWayActor.props();
    }

    private static class GateWayActor extends AbstractServiceActor {

        @Override
        public void onRequest(ApiRequest request) {
            //        switch (request.getApiOpcode().getNumber()) {
//            case GATEWAY_METHODS.PASSPPORT_CONNECT_VALUE:
//                RequestArg tokenArg = request.getArg(0);
//                if (tokenArg == null) {
//                    // token 转换错误
//                    ReplyUtils.reply(context, DOMAIN.GATE_WAY, ResponseMessage.newMessage(request.getApiOpcode(),
//                            MessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.TOKEN_PARSE_ERROR))));
//                    return;
//                }
//
////                    String token = tokenArg.getAsString();
////                    String parseToken = new String(BaseEncoding.base64Url().decode(token));
////                    List<String> tokenChars = Splitter.on('|').splitToList(parseToken);
////                    ContextAdapter.userLogin(context.getRawContext().channel().id(), Long.valueOf(tokenChars.get(0)));
//                ContextAdapter.userLogin(context.channel().id(), tokenArg.getAsLong());
//
//                // FIXME: 2017/8/30 登陆成功，返回用户状态，如果是 in game 就走重连机制
//                // 回复自己完成了操作
//                ReplyUtils.reply(context, DOMAIN.GATE_WAY, ResponseMessage.newMessage(request.getApiOpcode(), MessageProvider.produceMessage()));
//        }
        }

        public static Props props() {
            return Props.create(GateWayActor.class);
        }

    }
}