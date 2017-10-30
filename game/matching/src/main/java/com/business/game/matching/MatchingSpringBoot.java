package com.business.game.matching;

import com.github.freedompy.commelina.akka.dispatching.cluster.nodes.ClusterChildNodeSystemCreator;
import com.business.game.matching.router.MatchingRouter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 *
 * @author @panyao
 * @date 2017/8/29
 */
@SpringBootApplication
public class MatchingSpringBoot {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MatchingSpringBoot.class);
        app.setWebEnvironment(false);
        app.run(args);
    }

    @PostConstruct
    public void init() {
        ClusterChildNodeSystemCreator.create(MatchingRouter.class, "ClusterMatchingSystem", "cluster-matching");
    }

}
