package com.business.game.gateway.router;

import com.github.freedompy.commelina.akka.dispatching.local.AkkaLocalWorkerSystem;
import com.github.freedompy.commelina.akka.dispatching.proto.MemberOfflineEvent;
import com.github.freedompy.commelina.niosocket.MemberEventHandler;
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
            AkkaLocalWorkerSystem.INSTANCE.askLocalRouterNode(MemberOfflineEvent.newBuilder().setLogoutUserId(logoutUserId).build());
        }
    }

    @Override
    public void onException(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("channel id {}, netty server error {}", ctx.channel().id(), cause);
    }

}
