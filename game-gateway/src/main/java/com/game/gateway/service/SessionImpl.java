package com.game.gateway.service;

import com.framework.akka_cluster_router.AbstractServiceActor;
import com.framework.message.ApiRequestLogin;
import com.framework.message.BusinessMessage;
import com.framework.message.RequestArg;
import com.game.gateway.MessageProvider;
import com.game.gateway.proto.ERROR_CODE;
import com.game.gateway.proto.GATEWAY_METHODS;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/25.
 */
public class SessionImpl extends AbstractServiceActor {

    @Override
    public Internal.EnumLite getRouterId() {
        return GATEWAY_METHODS.PASSPORT_CONNECT;
    }

    @Override
    public void onRequest(ApiRequestLogin request) {
        RequestArg tokenArg = request.getArg(0);
        if (tokenArg == null) {
            // token 转换错误
            response(MessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.TOKEN_PARSE_ERROR)));
            return;
        }

//                    String token = tokenArg.getAsString();
//                    String parseToken = new String(BaseEncoding.base64Url().decode(token));
//                    List<String> tokenChars = Splitter.on('|').splitToList(parseToken);
//                    ContextAdapter.userLogin(context.getRawContext().channel().id(), Long.valueOf(tokenChars.get(0)));
//        ContextAdapter.userLogin(context.channel().id(), tokenArg.getAsLong());

        response(MessageProvider.produceMessage());
    }
}
