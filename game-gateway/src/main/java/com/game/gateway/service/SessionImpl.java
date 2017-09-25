package com.game.gateway.service;

import com.framework.akka_cluste_router.AbstractServiceActor;
import com.framework.akka_cluste_router.AkkaServiceRouter;
import com.framework.message.ApiRequest;
import com.framework.message.ResponseMessage;
import com.game.gateway.MessageProvider;

/**
 * Created by @panyao on 2017/9/25.
 */
@AkkaServiceRouter(routerId = 1)
public class SessionImpl extends AbstractServiceActor {

    @Override
    public void onRequest(ApiRequest request) {

        this.reply(ResponseMessage.newMessage(MessageProvider.produceMessage()));
    }

}
