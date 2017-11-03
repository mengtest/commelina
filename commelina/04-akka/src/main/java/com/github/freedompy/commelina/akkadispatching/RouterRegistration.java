package com.github.freedompy.commelina.akkadispatching;

import com.google.protobuf.Internal;

/**
 * @author panyao
 * @date 2017/10/17
 */
public class RouterRegistration {

    private final Internal.EnumLite routerId;

    public RouterRegistration(Internal.EnumLite routerId) {
        this.routerId = routerId;
    }

    public Internal.EnumLite getRouterId() {
        return routerId;
    }

}
