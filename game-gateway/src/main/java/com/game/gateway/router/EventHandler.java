package com.game.gateway.router;

import com.framework.akka.router.cluster.AkkaMultiWorkerSystem;
import com.framework.akka.router.cluster.AkkaMultiWorkerSystemContext;
import com.framework.akka.router.local.AkkaLocalWorkerSystem;
import com.framework.akka.router.proto.MemberOfflineEvent;
import com.framework.niosocket.MemberEventHandler;
import com.game.gateway.proto.FindLastAccessDomainRequest;
import com.game.gateway.proto.FindLastAccessDomainResponse;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author @panyao
 * @date 2017/9/22
 */
@Component
public class EventHandler implements MemberEventHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onOffline(long logoutUserId, ChannelHandlerContext ctx) {
        if (logoutUserId > 0) {
            // 获取用户最好访问的 domain
            FindLastAccessDomainResponse domain = (FindLastAccessDomainResponse) AkkaLocalWorkerSystem.INSTANCE.askLocalRouterNode(
                    FindLastAccessDomainRequest.newBuilder()
                            .setUserId(logoutUserId)
                            .build()
            );
            if (domain != null) {
                AkkaMultiWorkerSystem clusterSystem = AkkaMultiWorkerSystemContext.INSTANCE.getContext(domain.getDomainValue());
                if (clusterSystem != null) {
                    // 向远程发送下线通知
                    clusterSystem.askRouterClusterNode(MemberOfflineEvent.newBuilder().setLogoutUserId(logoutUserId).build());
                }
            }
        }
    }

    @Override
    public void onException(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("channel id {}, netty server error {}", ctx.channel().id(), cause);
    }

}
