package com.business.apis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import java.util.Map;

/**
 * @author @panyao
 * @date 2017/8/30
 */
@SpringBootApplication
@ImportResource(locations = {
        "classpath:spring-beans.xml",
        "classpath:spring-mvc.xml",
        "classpath:com/business/context/passport/data-beans.xml",
        "classpath:com/business/context/passport/data-redis-connection-factory.xml",
        "classpath:com/business/context/passport/data-jpa-session-factory.xml",
})
public class AppBoot {

    public static void main(String[] args) {
        SpringApplication.run(AppBoot.class, args);
    }

}