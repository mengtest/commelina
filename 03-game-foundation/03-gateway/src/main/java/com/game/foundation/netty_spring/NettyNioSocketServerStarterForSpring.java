package com.game.foundation.netty_spring;

import com.game.foundation.netty.NettyNioSocketServer;
import com.game.foundation.netty.router.DefaultRpcWithProtoBuff;
import org.springframework.context.ApplicationContext;

/**
 * Created by @panyao on 2017/8/4.
 */
public final class NettyNioSocketServerStarterForSpring {

    public static NettyNioSocketServer startWithProtoBuff(ApplicationContext context, int port) throws Exception {
        NettyNioSocketServer event = context.getBean(NettyNioSocketServer.class);

        DefaultRpcWithProtoBuff socketServerHandler = context.getBean(DefaultRpcWithProtoBuff.class);
        socketServerHandler.defaultSpringLoader(context);

        event.setPort(port);
        event.bind();
        return event;
    }


}
