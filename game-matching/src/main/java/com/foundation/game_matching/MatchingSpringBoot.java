package com.foundation.game_matching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
    public MatchingActorApp createMatchingActor() {
        return new MatchingActorApp();
    }

    @Bean
    public MatchingConfigEntity configEntity() {
        return new MatchingConfigEntity();
    }


}
