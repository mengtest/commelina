package com.twentyfourpoints.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by @panyao on 2017/9/1.
 */
@SpringBootApplication
@ImportResource(locations = {
        "classpath:platform-api-spring-beans.xml",
        "classpath:platform-api-spring-mvc.xml",
})
public class ApiBoot {

    public static void main(String[] args) {
        SpringApplication.run(ApiBoot.class);
    }

}