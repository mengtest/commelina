package com.commelina.server.passport;

import com.commelina.server.passport.interceptors.ApiInterceptor;
import com.commelina.server.passport.interceptors.OAuth2Interceptor;
import com.commelina.web.mvc.ExceptionHandlerResolverWithJsonOutput;
import com.commelina.web.mvc.JsonMessageConverterWithLanguage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;

/**
 * @author panyao
 * @date 2017/11/22
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiInterceptor()).addPathPatterns("/api**");
        registry.addInterceptor(new OAuth2Interceptor()).addPathPatterns("/oauth2**");
    }

    @Bean
    public HandlerExceptionResolver exceptionHandler() {
        return new ExceptionHandlerResolverWithJsonOutput();
    }

    @Bean
    public HttpMessageConverter converter() {
//        return new MappingJackson2HttpMessageConverter();
        return new JsonMessageConverterWithLanguage();
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(
                Charset.forName("UTF-8"));
        return converter;
    }

}
