package com.game.gateway;

import com.framework.niosocket.akka.NioSocketBootWithActor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by @panyao on 2017/8/10.
 */
@SpringBootApplication
public class GatewaySpringBoot {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GatewaySpringBoot.class);
        app.setWebEnvironment(false);
        app.run(args);
    }

    @Bean
    public NioSocketBootWithActor createServer() {
        return new NioSocketBootWithActor();
    }

    @Bean
    public AkkaRemoteActorEntity akkaRemoteAcotrEntity() {
        return new AkkaRemoteActorEntity();
    }

}
