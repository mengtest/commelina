package com.game.matching.service;

import akka.actor.Props;
import com.framework.akka_router.ServiceHandler;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/26.
 */
public class MatchingV4 implements ServiceHandler {

    @Override
    public Props getProps() {
        return null;
    }

    @Override
    public Internal.EnumLite getRouterId() {
        return null;
    }

}
