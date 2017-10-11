package com.game.matching.service;

import com.framework.akka_router.LocalServiceHandler;
import com.framework.akka_router.local.AbstractLocalServiceActor;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/26.
 */
public class MatchingV4 implements LocalServiceHandler {

    @Override
    public Class<AbstractLocalServiceActor> getPropsClass() {
        return null;
    }

    @Override
    public Internal.EnumLite getRouterId() {
        return null;
    }

}
