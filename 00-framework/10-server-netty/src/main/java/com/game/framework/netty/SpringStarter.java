package com.game.framework.netty;

import org.springframework.context.ApplicationContext;

/**
 * Created by @panyao on 2017/8/4.
 */
public final class SpringStarter {

    public static void start(ApplicationContext context, SessionInterface sessionInterface) throws Exception {
        GameMainEvent event = context.getBean(GameMainEvent.class);
        event.bind(9002, sessionInterface);
    }

}
