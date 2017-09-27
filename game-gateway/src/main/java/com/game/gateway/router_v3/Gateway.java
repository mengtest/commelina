package com.game.gateway.router_v3;

import com.framework.akka_router.DefaultLocalActorRequestHandler;
import com.framework.message.ApiRequest;
import com.framework.message.BusinessMessage;
import com.framework.message.DefaultMessageProvider;
import com.framework.message.ResponseMessage;
import com.framework.niosocket.ContextAdapter;
import com.framework.niosocket.NioSocketRouter;
import com.framework.niosocket.ReplyUtils;
import com.game.gateway.proto.DOMAIN;
import com.game.gateway.proto.ERROR_CODE;
import com.game.gateway.proto.GATEWAY_METHODS;
import com.google.protobuf.Internal;
import io.netty.channel.ChannelHandlerContext;


/**
 * Created by @panyao on 2017/9/22.
 */
@NioSocketRouter(forward = DOMAIN.GATE_WAY_VALUE)
public class Gateway extends DefaultLocalActorRequestHandler {

    @Override
    public Internal.EnumLite getRouterId() {
        return DOMAIN.GATE_WAY;
    }

    @Override
    public void onRequest(ApiRequest request, ChannelHandlerContext ctx) {
        switch (request.getOpcode().getNumber()) {
            // 登录接口允许匿名
            case GATEWAY_METHODS.PASSPORT_CONNECT_VALUE:
                super.onRequest(request, ctx);
                return;
        }

        final long userId = ContextAdapter.getLoginUserId(ctx.channel().id());
        if (userId <= 0) {
            ResponseMessage message = ResponseMessage.newMessage(
                    DefaultMessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.MATCHING_API_UNAUTHORIZED)));

            ReplyUtils.reply(ctx, DOMAIN.GATE_WAY, request.getOpcode(), message);
            return;
        }

        request.setUserId(userId);
        super.onRequest(request, ctx);
    }

//    private static class GateWayActor extends AbstractServiceActor {
//
//        @Override
//        public void onRequest(ApiRequest request) {
//            switch (request.getOpcode().getNumber()) {
//                // 改成注解的形式
//                case GATEWAY_METHODS.PASSPORT_CONNECT_VALUE:
//                    break;
////                RequestArg tokenArg = request.getArg(0);
////                if (tokenArg == null) {
////                    // token 转换错误
////                    ReplyUtils.response(context, DOMAIN.GATE_WAY, ResponseMessage.newMessage(request.getOpcode(),
////                            DefaultMessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.TOKEN_PARSE_ERROR))));
////                    return;
////                }
////
//////                    String token = tokenArg.getAsString();
//////                    String parseToken = new String(BaseEncoding.base64Url().decode(token));
//////                    List<String> tokenChars = Splitter.on('|').splitToList(parseToken);
//////                    ContextAdapter.userLogin(context.getRawContext().channel().id(), Long.valueOf(tokenChars.get(0)));
////                ContextAdapter.userLogin(context.channel().id(), tokenArg.getAsLong());
////
////                // FIXME: 2017/8/30 登陆成功，返回用户状态，如果是 in game 就走重连机制
////                // 回复自己完成了操作
////                ReplyUtils.response(context, DOMAIN.GATE_WAY, ResponseMessage.newMessage(request.getOpcode(), DefaultMessageProvider.produceMessage()));
//            }
//        }
//
//        public static Props props() {
//            return Props.create(GateWayActor.class);
//        }
//
//    }
}