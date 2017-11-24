package com.commelina.math24.play.robot;

import com.commelina.math24.play.robot.events.MatchingJoinMatch;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * @author @panyao
 * @date 2017/9/8
 */
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(App.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

    @PostConstruct
    public void init() throws InterruptedException {
        Starter.start("", "", new MatchingJoinMatch());
    }

}
