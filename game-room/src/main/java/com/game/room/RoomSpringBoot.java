package com.game.room;

import com.framework.akka_router.cluster.node.ClusterChildNodeSystemCreator;
import com.game.room.router_v3.RoomRouter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * Created by @panyao on 2017/8/29.
 */
@SpringBootApplication
public class RoomSpringBoot {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RoomSpringBoot.class);
        app.setWebEnvironment(false);
        app.run(args);
    }

    @PostConstruct
    public void init() {
        ClusterChildNodeSystemCreator.create(RoomRouter.class);
    }

}
