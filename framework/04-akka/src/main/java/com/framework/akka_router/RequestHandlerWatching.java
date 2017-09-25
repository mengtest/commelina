package com.framework.akka_router;

import com.framework.message.ApiRequest;

/**
 * Created by @panyao on 2017/9/25.
 */
public interface RequestHandlerWatching {

    void onRequest(ApiRequest request);

}
