package com.game.matching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by @panyao on 2017/8/29.
 */
@SpringBootApplication
public final class MatchingSpringBoot {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MatchingSpringBoot.class);
        app.setWebEnvironment(false);
        app.run(args);
    }

}
