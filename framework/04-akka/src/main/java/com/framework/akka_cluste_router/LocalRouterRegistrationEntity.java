package com.framework.akka_cluste_router;

import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/25.
 */
public final class LocalRouterRegistrationEntity {

    private final Internal.EnumLite routerId;

    public LocalRouterRegistrationEntity(Internal.EnumLite routerId) {
        this.routerId = routerId;
    }

    public Internal.EnumLite getRouterId() {
        return routerId;
    }

}
