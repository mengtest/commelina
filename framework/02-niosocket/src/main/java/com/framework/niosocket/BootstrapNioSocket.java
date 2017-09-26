package com.framework.niosocket;

import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;

/**
 * Created by @panyao on 2017/8/4.
 *
 *  实现ApplicationContextAware以获得ApplicationContext中的所有bean
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

        Map<String, Object> routers = context.getBeansWithAnnotation(NioSocketRouter.class);
        Map<Integer, RequestHandler> handlerMap = Maps.newHashMap();

        for (Object o : routers.values()) {
            NioSocketRouter controller = o.getClass().getAnnotation(NioSocketRouter.class);
            int forward = controller.forward();
            if (o instanceof RequestHandler) {
                handlerMap.put(forward, (RequestHandler) o);
            } else {
                throw new RuntimeException("Undefined type " + o);
            }
        }

        MemberEventHandler memberEventHandler = context.getBean(MemberEventHandler.class);

        RouterContextHandlerImpl routerContextHandler = new RouterContextHandlerImpl();
        routerContextHandler.addRequestHandlers(handlerMap);
        server.bind(host, port, routerContextHandler, memberEventHandler);
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
