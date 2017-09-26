package com.framework.akka_router;

import com.framework.message.ApiRequest;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/25.
 */
public class ClusterRouterJoinEntity {

    private final Internal.EnumLite routerId;
    private final byte seedNode;

    private final ApiRequest apiRequest;

    public ClusterRouterJoinEntity(Internal.EnumLite routerId, byte seedNode, ApiRequest apiRequest) {
        this.routerId = routerId;
        this.seedNode = seedNode;
        this.apiRequest = apiRequest;
    }

    public Internal.EnumLite getRouterId() {
        return routerId;
    }

    public ApiRequest getApiRequest() {
        return apiRequest;
    }

    public byte getSeedNode() {
        return seedNode;
    }
}
