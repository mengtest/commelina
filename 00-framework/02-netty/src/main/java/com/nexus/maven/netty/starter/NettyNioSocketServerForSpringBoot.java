package com.nexus.maven.netty.starter;

import com.google.common.collect.Maps;
import com.nexus.maven.netty.socket.*;
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
 */
// 实现ApplicationContextAware以获得ApplicationContext中的所有bean
public final class NettyNioSocketServerForSpringBoot implements ApplicationContextAware {

    @Value("${nioSocketServer.host:127.0.0.1}")
    private String host;

    @Value("${nioSocketServer.port:9001}")
    private int port;

    private ApplicationContext context;

    private NettyNioSocketServer server;

    @PostConstruct
    public void initServer() throws IOException {
        server = new NettyNioSocketServer();

        Map<String, Object> apis = context.getBeansWithAnnotation(ActorWithApiController.class);
        Map<String, ActorWithApiHandler> actorWithApiHandlers = Maps.newHashMap();

        for (Object o : apis.values()) {
            ActorWithApiController controller = o.getClass().getAnnotation(ActorWithApiController.class);
            String apiName = controller.apiName();
            if (o instanceof ActorWithApiHandler) {
                actorWithApiHandlers.put(apiName, (ActorWithApiHandler) o);
            } else{
                throw new RuntimeException("undefined type " + o);
            }
        }

        ActorAkkaContext router = new ActorAkkaContext();
        router.initRouters(actorWithApiHandlers);

        server.bind(host, port, router);
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
