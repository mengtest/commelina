package com.commelina.server.passportv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * @author panyao
 * @date 2017/11/22
 */
@SpringBootApplication
@ComponentScan(basePackages = {
        "com.commelina.server.passport",
        "com.commelina.server.passportv2",
})
@ImportResource(locations = {
        "classpath:spring-mvc.xml",
//        "classpath:data-redis-connection-factory.xml",
        "classpath:data-jpa-session-factory.xml",
})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
