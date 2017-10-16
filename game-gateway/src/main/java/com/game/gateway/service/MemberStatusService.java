package com.game.gateway.service;

import com.framework.akka_router.LocalServiceHandler;
import com.framework.akka_router.local.AbstractLocalServiceActor;
import com.framework.niosocket.proto.SocketASK;
import com.google.common.collect.Maps;
import com.google.protobuf.Internal;

import java.util.Map;

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
        return MemberStatusActor.class;
    }

    private static class MemberStatusActor extends AbstractLocalServiceActor {

        private final Map<Long, Internal.EnumLite> userLastAccessServer = Maps.newHashMap();

        public MemberStatusActor(Internal.EnumLite routerId) {
            super(routerId);
        }

        @Override
        public void onRequest(SocketASK request) {
            // in game 逻辑
        }

    }

}
