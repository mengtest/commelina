package com.game.gateway;

import com.github.freedompy.commelina.akka.dispatching.LocalServiceHandler;
import com.github.freedompy.commelina.akka.dispatching.cluster.AkkaMultiWorkerSystemCreator;
import com.github.freedompy.commelina.akka.dispatching.local.AkkaLocalWorkerSystemCreator;
import com.github.freedompy.commelina.niosocket.BootstrapNioSocket;
import com.game.gateway.router.ProxyRoom;
import com.message.common.proto.DOMAIN;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

/**
 *
 * @author @panyao
 * @date 2017/8/10
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
        AkkaLocalWorkerSystemCreator.create("gateway")
                .registerRouter(applicationContext.getBeansOfType(LocalServiceHandler.class));

        // matching 集群 handler
        AkkaMultiWorkerSystemCreator.create(DOMAIN.MATCHING, "ClusterMatchingSystem", "cluster-gateway-matching")
                .registerRouter()
                .building();

//        // room 集群 handler
        AkkaMultiWorkerSystemCreator.create(DOMAIN.GAME_ROOM, "ClusterRoomSystem","cluster-gateway-room")
                .registerRouter(ProxyRoom.RoomRouterFrontedClusterActor.class)
                .building();
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}