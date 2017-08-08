package com.game.foundation.netty_spring;

import com.game.foundation.netty.NettyNioSocketServer;
import com.game.foundation.netty.RPCRouterDispatchInterface;
import org.springframework.context.ApplicationContext;

/**
 * Created by @panyao on 2017/8/4.
 */
public final class NettyNioSocketServerStarterForSpring {

    public static NettyNioSocketServer start(ApplicationContext context, int port,
                                             RPCRouterDispatchInterface dispatchInterface) throws Exception {
        NettyNioSocketServer event = context.getBean(NettyNioSocketServer.class);
        event.setPort(port);
        event.setDispatch(dispatchInterface);
        event.bind();
        return event;
    }


}
