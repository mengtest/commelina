package com.nexus.maven.netty_starter;

import com.nexus.maven.netty.NettyNioSocketServer;
import com.nexus.maven.netty.router.DefaultRpcWithProtoBuff;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by @panyao on 2017/8/4.
 */
@Component
// 实现ApplicationContextAware以获得ApplicationContext中的所有bean
@ImportResource(locations = {"classpath:spring-netty-context.xml"})
//@ComponentScan("com.nexus.maven.netty")
public final class NettyNioSocketServerForSpringBoot implements ApplicationContextAware {

    @Value("${nioSocketServer.host:127.0.0.1}")
    private String host;

    @Value("${nioSocketServer.port:9001}")
    private int port;

    private ApplicationContext context;

    private NettyNioSocketServer server;

    @PostConstruct
    public void initServer() throws Exception {
        server = context.getBean(NettyNioSocketServer.class);
        DefaultRpcWithProtoBuff socketServerHandler = context.getBean(DefaultRpcWithProtoBuff.class);
        socketServerHandler.defaultSpringLoader(context);
        server.bind(host, port);
    }

    @PreDestroy
    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
