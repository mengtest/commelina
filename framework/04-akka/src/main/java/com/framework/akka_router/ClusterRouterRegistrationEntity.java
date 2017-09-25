package com.framework.akka_router;

import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/25.
 */
public final class ClusterRouterRegistrationEntity {

    private final Internal.EnumLite routerId;
    private final byte seedNode;

    public ClusterRouterRegistrationEntity(Internal.EnumLite routerId, byte seedNode) {
        this.routerId = routerId;
        this.seedNode = seedNode;
    }

    public Internal.EnumLite getRouterId() {
        return routerId;
    }

    public byte getSeedNode() {
        return seedNode;
    }
}
