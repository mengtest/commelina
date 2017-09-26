package com.framework.akka_cluster_router;

import com.framework.message.ApiRequestLogin;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/25.
 */
public class LocalRouterJoinEntity {

    private final Internal.EnumLite routerId;

    private final ApiRequestLogin apiRequest;

    public LocalRouterJoinEntity(Internal.EnumLite routerId, ApiRequestLogin apiRequest) {
        this.routerId = routerId;
        this.apiRequest = apiRequest;
    }

    public Internal.EnumLite getRouterId() {
        return routerId;
    }

    public ApiRequestLogin getApiRequest() {
        return apiRequest;
    }
}
