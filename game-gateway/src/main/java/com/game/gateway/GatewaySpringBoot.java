package com.game.gateway;

import com.framework.akka_router.local.ServiceHandler;
import com.framework.akka_router.cluster.AkkaMultiWorkerSystemCreator;
import com.framework.akka_router.local.AkkaLocalWorkerSystemCreator;
import com.framework.niosocket.BootstrapNioSocket;
import com.game.gateway.proto.DOMAIN;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

/**
 * Created by @panyao on 2017/8/10.
 */
@SpringBootApplication
public class GatewaySpringBoot implements ApplicationContextAware {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GatewaySpringBoot.class);
        app.setWebEnvironment(false);
        app.run(args);
    }

    @Bean
    public BootstrapNioSocket createServer() {
        return new BootstrapNioSocket();
    }

    @PostConstruct
    public void init() {

        // 本地 handler
        AkkaLocalWorkerSystemCreator.create()
                .registerRouter(applicationContext.getBeansOfType(ServiceHandler.class));

        // matching 集群 handler
        AkkaMultiWorkerSystemCreator.create(DOMAIN.MATCHING, "cluster-matching")
                .registerRouter()
                .building();

        // matching 集群 handler
//        AkkaLocalWorkerSystemCreator.registerCluster(DOMAIN.MATCHING, RouterFrontendClusterActor.class);

        // room 集群 handler
//        AkkaLocalWorkerSystemCreator.registerCluster(DOMAIN.GAME_ROOM, RouterFrontendClusterActor.class);
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}