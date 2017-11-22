package com.commelina.web.mvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author panyao
 * @date 2017/11/22
 */
@Configuration
@EnableWebMvc
public class DefaultWebMvcConfig implements WebMvcConfigurer {

    @Bean
    public HandlerExceptionResolver methodExceptionHandler() {
        return new MethodExceptionHandlerResolverForJson();
    }

}
