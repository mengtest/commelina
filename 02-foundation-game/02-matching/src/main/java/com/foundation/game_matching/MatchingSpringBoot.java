package com.foundation.game_matching;

import akka.actor.ActorSystem;
import com.foundation.game_matching.portal.MatchingRouter;
import com.typesafe.config.ConfigFactory;
import org.springframework.beans.factory.annotation.Value;
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

    private static final class MatchingApp {

        @Value("matching:queue:successPeople:10")
        private int matchSucessPeople;

        @PostConstruct
        public void init() {
            // 初始化配置
            configEntity = new MatchingConfigEntity(matchSucessPeople);

            ActorSystem system = ActorSystem.create("MatchingWorkerSystem",
                    ConfigFactory.load(("matching")));
            system.actorOf(MatchingRouter.props(), "matchingRouter");
        }

    }

    private static MatchingConfigEntity configEntity;

    public static MatchingConfigEntity getConfigEntity() {
        return configEntity;
    }
}
