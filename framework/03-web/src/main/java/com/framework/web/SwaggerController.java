package com.framework.web;

import org.springframework.context.annotation.Bean;

/**
 * Created by @panyao on 2017/9/4.
 */
@Deprecated
public class SwaggerController {

    @Bean
    public SwaggerConfigWithSpringMvc configWithSpringMvc() {
        return new SwaggerConfigWithSpringMvc();
    }

}
