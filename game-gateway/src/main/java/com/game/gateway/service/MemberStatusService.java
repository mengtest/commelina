package com.game.gateway.service;

import com.framework.akka_router.LocalServiceHandler;
import com.framework.akka_router.local.AbstractLocalServiceActor;
import com.framework.message.ApiRequest;
import com.google.protobuf.Internal;

/**
 * Created by panyao on 2017/8/30.
 * <p>
 * 记录用户状态，是匹配中，还是游戏中
 */
//@ActorServiceHandler
public class MemberStatusService implements LocalServiceHandler {

    @Override
    public Internal.EnumLite getRouterId() {
        return null;
    }

    @Override
    public Class<? extends AbstractLocalServiceActor> getPropsClass() {
        return null;
    }

    private static class MemberStatusActor extends AbstractLocalServiceActor {

        public MemberStatusActor(Internal.EnumLite routerId) {
            super(routerId);
        }

        @Override
        public void onRequest(ApiRequest request) {

        }

    }
}
