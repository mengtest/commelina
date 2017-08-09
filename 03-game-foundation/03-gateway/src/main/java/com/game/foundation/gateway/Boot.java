package com.game.foundation.gateway;

import com.game.foundation.netty.NettyNioSocketServer;
import com.game.foundation.netty_spring.NettyNioSocketServerStarterForSpring;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by @panyao on 2017/8/8.
 */
public final class Boot {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/spring-*.xml");
        NettyNioSocketServer server = null;
        try {
            server = NettyNioSocketServerStarterForSpring.startWithProtoBuff(context, 9001);
        } catch (Exception e) {
            e.printStackTrace();
            if (server != null) {
                server.shutdown();
            }
        }
    }

}
