package com.commelina.math24.play.match;

import com.commelina.akka.dispatching.nodes.ClusterChildNodeSystemCreator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 *
 * @author @panyao
 * @date 2017/8/29
 */
@SpringBootApplication
public class AppBoot {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AppBoot.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

    @PostConstruct
    public void init() {
        ClusterChildNodeSystemCreator.create(MatchPortal.class, "ClusterMatchingSystem", "cluster-match");
    }

}
