package com.nexus.maven.netty.socket;

import com.nexus.maven.core.message.ApiRequest;
import com.nexus.maven.core.message.MessageBus;

/**
 * Created by @panyao on 2017/8/25.
 */
public interface ActorWithApiHandler extends ActorRouterHandler {

    RequestEvent getRouterEvent();

    interface RequestEvent {
        MessageBus onRequest(ApiRequest request, ActorOutputContext context);
    }

}
