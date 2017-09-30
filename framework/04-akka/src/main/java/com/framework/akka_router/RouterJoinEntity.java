package com.framework.akka_router;

import com.framework.message.ApiRequest;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/25.
 */
public class RouterJoinEntity {

    private final Internal.EnumLite routerId;

    private final ApiRequest apiRequest;

    public RouterJoinEntity(Internal.EnumLite routerId, ApiRequest apiRequest) {
        this.routerId = routerId;
        this.apiRequest = apiRequest;
    }

    public Internal.EnumLite getRouterId() {
        return routerId;
    }

    public ApiRequest getApiRequest() {
        return apiRequest;
    }
}
