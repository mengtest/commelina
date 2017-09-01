package com.foundation.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by @panyao on 2017/8/30.
 */
@SpringBootApplication
public class AppBoot {

    public static void main(String[] args) {
        SpringApplication.run(AppBoot.class);
    }

    @Bean
    public PlatformConfig platformConfig() {
        return new PlatformConfig();
    }

}