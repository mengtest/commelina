package com.game.gateway.router_v3;

import com.framework.niosocket.MemberEventHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * Created by @panyao on 2017/9/22.
 */
@Component
public class EventHandler implements MemberEventHandler {

    @Override
    public void onOnline(ChannelHandlerContext ctx) {

    }

    @Override
    public void onOffline(long logoutUserId, ChannelHandlerContext ctx) {

    }

    @Override
    public void onException(ChannelHandlerContext ctx, Throwable cause) {
        throw new RuntimeException(cause);
    }

}
