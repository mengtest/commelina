package com.framework.akka_cluster_router;

import com.framework.message.ApiRequest;

/**
 * Created by @panyao on 2017/9/25.
 */
public interface OnRequest {

    void onRequest(ApiRequest request);

}
