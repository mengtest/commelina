package com.business.apis;

import com.github.freedompy.commelina.data.RedisSessionHandlerImpl;
import com.github.freedompy.commelina.webmvc.SessionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * @author @panyao
 * @date 2017/8/30
 */
@SpringBootApplication
@ImportResource(locations = {
        "classpath:spring-beans.xml",
        "classpath:spring-mvc.xml",
//        "classpath:com/business/service/uc/data-redis-connection-factory.xml",
//        "classpath:com/business/service/uc/data-jpa-session-factory.xml",
//        "classpath:com/business/service/uc/data-beans.xml",
})
@ComponentScan(basePackages = {"com.business.service.uc", "com.business.apis"})
public class AppBoot {

    public static void main(String[] args) {
        SpringApplication.run(AppBoot.class);
    }

    @Bean
    public SessionHandler sessionHandler() {
        return new RedisSessionHandlerImpl();
    }

}