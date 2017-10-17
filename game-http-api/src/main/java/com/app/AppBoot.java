package com.app;

import com.framework.data.CacheSessionHandlerImpl;
import com.framework.web.SessionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 *
 * @author @panyao
 * @date 2017/8/30
 */
@SpringBootApplication
@ImportResource(locations = {
        "classpath:spring-beans.xml",
        "classpath:spring-mvc.xml",
        "classpath:data-redis-connection-factory.xml",
        "classpath:data-jpa-session-factory.xml",
        "classpath:data-beans.xml",
})
@ComponentScan("com.framework.data")
public class AppBoot {

    public static void main(String[] args) {

        SpringApplication.run(AppBoot.class);
    }

    @Bean
    public SessionHandler sessionHandler() {
        return new CacheSessionHandlerImpl();
    }

}