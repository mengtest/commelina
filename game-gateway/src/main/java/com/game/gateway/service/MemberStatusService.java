package com.game.gateway.service;

import akka.japi.pf.ReceiveBuilder;
import com.framework.akka.router.ActorServiceHandler;
import com.framework.akka.router.LocalServiceHandler;
import com.framework.akka.router.local.AbstractLocalServiceActor;
import com.framework.akka.router.proto.ApiRequest;
import com.google.protobuf.Internal;

/**
 * @author panyao
 * @date 2017/8/30
 * <p>
 * 记录用户状态，是匹配中，还是游戏中
 */
@ActorServiceHandler
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

        public MemberStatusActor(Internal.EnumLite routerId) {
            super(routerId);
        }

        @Override
        public void onRequest(ApiRequest request) {

            // in game 逻辑
        }

        @Override
        protected ReceiveBuilder addLocalMatch(ReceiveBuilder builder) {
            return builder.match(Long.class, roomId -> {
                        getSender().tell(Boolean.TRUE, getSelf());
                    });
        }

    }

}
