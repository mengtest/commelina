package com.framework.akka;

import com.framework.message.ApiRouterRequest;

/**
 * Created by @panyao on 2017/9/6.
 */
public interface ActorWatching {

    void onRequest(ApiRouterRequest request);

}
