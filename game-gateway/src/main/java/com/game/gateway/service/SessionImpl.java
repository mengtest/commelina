package com.game.gateway.service;

import com.framework.akka_cluster_router.AbstractServiceActor;
import com.framework.message.ApiRequestLogin;
import com.framework.message.ResponseMessage;
import com.game.gateway.MessageProvider;
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

        reply(ResponseMessage.newMessage(MessageProvider.produceMessage()));
    }
}
