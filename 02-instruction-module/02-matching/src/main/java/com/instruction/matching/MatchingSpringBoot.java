package com.instruction.matching;

import akka.actor.ActorSystem;
import com.instruction.matching.portal.MatchingRouter;
import com.typesafe.config.ConfigFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

/**
 * Created by @panyao on 2017/8/29.
 */
@SpringBootApplication
public class MatchingSpringBoot {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MatchingSpringBoot.class);
        app.setWebEnvironment(false);
        app.run(args);
    }

    @Bean
    public MatchingApp createMatchingActor() {
        return new MatchingApp();
    }

    public static class MatchingApp {

        @PostConstruct
        public void init() {
            ActorSystem system = ActorSystem.create("MatchingWorkerSystem",
                    ConfigFactory.load(("matching")));

            system.actorOf(MatchingRouter.props(), "matchingRouter");
        }

    }
}
