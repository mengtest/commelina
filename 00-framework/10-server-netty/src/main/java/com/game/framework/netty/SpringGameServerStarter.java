package com.game.framework.netty;

import org.springframework.context.ApplicationContext;

/**
 * Created by @panyao on 2017/8/4.
 */
public final class SpringGameServerStarter {

    public static void start(ApplicationContext context, int port,
                             SessionInterface sessionInterface,
                             RPCRouterDispatchInterface dispatchInterface) throws Exception {
        GameServer event = context.getBean(GameServer.class);
        event.setPort(port);
        event.setSessionInterface(sessionInterface);
        event.setDispatch(dispatchInterface);
        event.bind();
    }


}
