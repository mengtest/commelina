package com.game.matching;

import com.framework.akka_router.cluster.node.ClusterChildNodeSystemCreator;
import com.game.matching.router_v3.MatchingRouter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * Created by @panyao on 2017/8/29.
 */
@SpringBootApplication
public final class MatchingSpringBoot{

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MatchingSpringBoot.class);
        app.setWebEnvironment(false);
        app.run(args);
    }

    @PostConstruct
    public void init() {
        ClusterChildNodeSystemCreator.create(MatchingRouter.class);
    }

}
