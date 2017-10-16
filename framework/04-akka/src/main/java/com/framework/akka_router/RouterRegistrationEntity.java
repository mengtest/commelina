package com.framework.akka_router;

import com.google.protobuf.Internal;

import java.io.Serializable;

/**
 * Created by @panyao on 2017/9/25.
 */
public final class RouterRegistrationEntity implements Serializable {

    private final Internal.EnumLite routerId;

    public RouterRegistrationEntity(Internal.EnumLite routerId) {
        this.routerId = routerId;
    }

    public Internal.EnumLite getRouterId() {
        return routerId;
    }

}
