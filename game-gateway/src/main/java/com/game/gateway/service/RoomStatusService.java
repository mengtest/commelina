package com.game.gateway.service;

import akka.japi.pf.ReceiveBuilder;
import com.framework.akka.router.ActorServiceHandler;
import com.framework.akka.router.LocalServiceHandler;
import com.framework.akka.router.local.AbstractLocalServiceActor;
import com.framework.akka.router.proto.ApiRequest;
import com.game.gateway.proto.*;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.protobuf.Internal;

/**
 * @author panyao
 * @date 2017/8/30
 * <p>
 * 记录用户状态，是匹配中，还是游戏中
 */
@ActorServiceHandler
public class RoomStatusService implements LocalServiceHandler {

    @Override
    public Internal.EnumLite getRouterId() {
        return null;
    }

    @Override
    public Class<? extends AbstractLocalServiceActor> getPropsClass() {
        return MemberStatusActor.class;
    }

    private static class MemberStatusActor extends AbstractLocalServiceActor {

        private final BiMap<Long, DOMAIN> accessDomain = HashBiMap.create();

        public MemberStatusActor(Internal.EnumLite routerId) {
            super(routerId);
        }

        @Override
        public void onRequest(ApiRequest request) {

            // in game 逻辑
        }

        @Override
        protected ReceiveBuilder addLocalMatch(ReceiveBuilder builder) {
            return builder
                    .match(FindRoomRequest.class, roomId -> {


                        getSender().tell(FindRoomResponse.getDefaultInstance(), getSelf());
                    })
                    // 查询最后访问的 domain
                    .match(FindLastAccessDomainRequest.class, r -> {
                        DOMAIN domain = accessDomain.get(r.getUserId());
                        if (domain == null) {
                            getSender().tell(FindLastAccessDomainResponse.getDefaultInstance(), getSelf());
                        } else {
                            getSender().tell(FindLastAccessDomainResponse.newBuilder()
                                            .setDomain(domain)
                                            .build()
                                    , getSelf());
                        }
                    })
                    // 重置访问的 domain
                    .match(ResetAccesssDoamin.class, ra -> accessDomain.remove(ra.getUserId()));
        }

    }

}