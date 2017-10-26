package com.game.gateway.service;

import akka.japi.pf.ReceiveBuilder;
import com.github.freedompy.commelina.akka.dispatching.ActorServiceHandler;
import com.github.freedompy.commelina.akka.dispatching.LocalServiceHandler;
import com.github.freedompy.commelina.akka.dispatching.local.AbstractLocalServiceActor;
import com.game.gateway.proto.*;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.protobuf.Internal;
import com.message.common.proto.DOMAIN;

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
        return GATEWAY_METHODS.MEMEBER_STATUS;
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
        protected ReceiveBuilder addLocalMatch(ReceiveBuilder builder) {
            return builder
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
                    .match(ResetAccesssDoamin.class, ra -> accessDomain.remove(ra.getUserId()))
                    .match(ChangeAccesssDoamin.class, ca -> accessDomain.put(ca.getUserId(), ca.getDomain()));
        }

    }

}