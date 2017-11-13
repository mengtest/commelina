package com.commelina.math24.play.room;

import com.commelina.akka.dispatching.cluster.nodes.ClusterChildNodeSystemCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * @author @panyao
 * @date 2017/8/29
 */
@SpringBootApplication
public class AppBoot {

    @Value("${checkerboard.size.x:100}")
    private int checkerboardSizeX;

    @Value("${checkerboard.size.x:100}")
    private int checkerboardSizeY;

    @Value("${room.checkOeverMinute:10}")
    private int roomCheckOeverMinute;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AppBoot.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

    @PostConstruct
    public void init() {
        ClusterChildNodeSystemCreator.create(
                RoomPortal.class,
                "ClusterRoomSystem",
                "cluster-room");
    }

}
