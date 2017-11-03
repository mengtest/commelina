package com.business.game.gateway.service;

import akka.japi.pf.ReceiveBuilder;
import com.business.game.message.common.proto.DOMAIN;
import com.game.gateway.proto.*;
import com.github.freedompy.commelina.akkadispatching.ActorServiceHandler;
import com.github.freedompy.commelina.akkadispatching.LocalServiceHandler;
import com.github.freedompy.commelina.akkadispatching.cluster.AkkaMultiWorkerSystem;
import com.github.freedompy.commelina.akkadispatching.cluster.AkkaMultiWorkerSystemContext;
import com.github.freedompy.commelina.akkadispatching.local.AbstractLocalServiceActor;
import com.github.freedompy.commelina.akkadispatching.proto.MemberOfflineEvent;
import com.github.freedompy.commelina.akkadispatching.proto.MemberOnlineEvent;
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
                    // 用户上线
                    .match(MemberOnlineEvent.class, on -> {
                        DOMAIN domain = accessDomain.get(on.getLoginUserId());
                        // 获取用户后的 domain
                        if (domain != null) {
                            AkkaMultiWorkerSystem clusterSystem = AkkaMultiWorkerSystemContext.INSTANCE.getContext(domain.getNumber());
                            if (clusterSystem != null) {
                                // 向远程发送下线通知
                                clusterSystem.askRouterClusterNode(on);
                            }
                        }
                    })
                    // 用户下线
                    .match(MemberOfflineEvent.class, off -> {
                        DOMAIN domain = accessDomain.get(off.getLogoutUserId());
                        // 获取用户后的 domain
                        if (domain != null) {
                            AkkaMultiWorkerSystem clusterSystem = AkkaMultiWorkerSystemContext.INSTANCE.getContext(domain.getNumber());
                            if (clusterSystem != null) {
                                // 向远程发送下线通知
                                clusterSystem.askRouterClusterNode(off);
                            }
                        }
                        // 重置用户访问的 domain
                        accessDomain.remove(off.getLogoutUserId());
                    })
                    // 更新用户最后访问的 domain
                    .match(ChangeAccesssDoamin.class, ca -> accessDomain.put(ca.getUserId(), ca.getDomain()));
        }

    }


}