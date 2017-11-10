package org.commelina.match24.play.robot;

import org.commelina.match24.play.robot.interfaces.MainGameEvent;
import org.commelina.match24.play.robot.interfaces.MainHandler;
import org.commelina.match24.play.robot.events.GatewayLogin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 *
 * @author @panyao
 * @date 2017/9/8
 */
@SpringBootApplication
public class RobotSpringBoot {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RobotSpringBoot.class);
        app.setWebEnvironment(false);
        app.run(args);
    }

    @PostConstruct
    public void init() {
        MainGameEvent event = new MainHandler();
        event.start(new GatewayLogin("", ""));
    }

}
