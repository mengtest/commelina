package com.twofourpoints.room;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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

    @Bean
    public MatchingApp createMatchingActor() {
        return new MatchingApp();
    }

    public static class MatchingApp {

        @PostConstruct
        public void init() {

        }

    }
}
