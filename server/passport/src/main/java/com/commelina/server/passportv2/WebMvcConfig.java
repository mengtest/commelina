package com.commelina.server.passportv2;

import com.commelina.server.passportv2.controller.ApiInterceptor;
import com.commelina.server.passportv2.controller.OAuth2Interceptor;
import com.commelina.web.mvc.DefaultWebMvcConfig;
import com.commelina.web.mvc.JacksonResponseBodyJsonAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.nio.charset.Charset;

/**
 * @author panyao
 * @date 2017/11/22
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig extends DefaultWebMvcConfig{

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiInterceptor()).addPathPatterns("/api**");
        registry.addInterceptor(new OAuth2Interceptor()).addPathPatterns("/oauth2**");
    }

    @Bean
    public ResponseBodyAdvice<Object> converter() {
//        return new MappingJackson2HttpMessageConverter();
        return new JacksonResponseBodyJsonAdvice();
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(
                Charset.forName("UTF-8"));
        return converter;
    }

}
