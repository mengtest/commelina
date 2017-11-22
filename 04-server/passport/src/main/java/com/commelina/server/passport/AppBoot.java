package com.commelina.server.passport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * @author @panyao
 * @date 2017/8/30
 */
@SpringBootApplication
@ComponentScan(basePackages = {
        "com.commelina.server.passport",
})
@ImportResource(locations = {
        "classpath:spring-mvc.xml",
//        "classpath:data-redis-connection-factory.xml",
        "classpath:data-jpa-session-factory.xml",
})
public class AppBoot {

    public static void main(String[] args) {
        SpringApplication.run(AppBoot.class, args);
    }

}