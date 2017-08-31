package com.foundation.app;

import com.framework.webmvc.JsonMessageConverter;
import com.framework.webmvc.ResponseBodyJsonAdvice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by @panyao on 2017/8/30.
 */
@SpringBootApplication
public class AppBoot {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AppBoot.class);
        app.setWebEnvironment(false);
        app.run(args);
    }

    @Bean
    public ResponseBodyJsonAdvice createJsonAdvice() {
        return new ResponseBodyJsonAdvice();
    }

    @Bean
    public JsonMessageConverter converter() {
        return new JsonMessageConverter();
    }

}
