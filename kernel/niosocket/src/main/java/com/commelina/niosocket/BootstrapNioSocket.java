package com.commelina.niosocket;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * 实现ApplicationContextAware以获得ApplicationContext中的所有bean
 * nio socket 启动类，基于spring bean
 *
 * @author @panyao
 * @date 2017/8/4
 */
public final class BootstrapNioSocket implements ApplicationContextAware {

    @Value("${nioSocketServer.host:127.0.0.1}")
    private String host;

    @Value("${nioSocketServer.port:9001}")
    private int port;

    private ApplicationContext context;

    private NettyNioSocketServer server;

    @PostConstruct
    public void initServer() throws IOException {
        server = new NettyNioSocketServer();
        SocketEventHandler socketEventHandler = context.getBean(SocketEventHandler.class);
        server.bindAndStart(host, port, socketEventHandler);
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

}
