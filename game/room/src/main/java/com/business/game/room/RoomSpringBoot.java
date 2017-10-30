package com.business.game.room;

import com.github.freedompy.commelina.akka.dispatching.cluster.nodes.ClusterChildNodeSystemCreator;
import com.business.game.room.router.RoomRouter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * @author @panyao
 * @date 2017/8/29
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
        ClusterChildNodeSystemCreator.create(RoomRouter.class, "ClusterRoomSystem", "cluster-room");
    }

}
