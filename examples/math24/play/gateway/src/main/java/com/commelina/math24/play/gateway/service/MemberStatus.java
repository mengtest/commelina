package com.commelina.math24.play.gateway.service;

import com.commelina.akka.dispatching.nodes.AbstractServiceActor;
import com.commelina.akka.dispatching.proto.MemberOfflineEvent;
import com.commelina.akka.dispatching.proto.MemberOnlineEvent;
import com.commelina.math24.common.proto.DOMAIN;
import com.game.gateway.proto.ChangeAccesssDoamin;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * @author panyao
 * @date 2017/11/16
 */
public class MemberStatus extends AbstractServiceActor {

    private final BiMap<Long, DOMAIN> accessDomain = HashBiMap.create();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                // 用户上线
                .match(MemberOnlineEvent.class, on -> {
                    DOMAIN domain = accessDomain.get(on.getLoginUserId());
                    // 获取用户后的 domain

                })
                // 用户下线
                .match(MemberOfflineEvent.class, off -> {
                    DOMAIN domain = accessDomain.get(off.getLogoutUserId());
                    // 获取用户后的 domain
                    if (domain != null) {

                    }
                    // 重置用户访问的 domain
                    accessDomain.remove(off.getLogoutUserId());
                })
                // 更新用户最后访问的 domain
                .match(ChangeAccesssDoamin.class, ca -> accessDomain.put(ca.getUserId(), ca.getDomain()))
                .build();
    }
}
