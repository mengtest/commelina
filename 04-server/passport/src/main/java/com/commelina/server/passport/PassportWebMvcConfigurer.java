package com.commelina.server.passport;

import com.commelina.server.passport.interceptors.ApiInterceptor;
import com.commelina.server.passport.interceptors.OAuth2Interceptor;
import com.commelina.web.mvc.ExceptionHandlerResolverWithJsonOutput;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * @author panyao
 * @date 2017/11/22
 */
@Configuration
@EnableWebMvc
public class PassportWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ApiInterceptor()).addPathPatterns("/api**");
        registry.addInterceptor(new OAuth2Interceptor()).addPathPatterns("/oauth2**");
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/classes/views/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setViewClass(JstlView.class);
        return viewResolver;
    }

    @Bean
    public HandlerExceptionResolver exceptionHandler() {
        return new ExceptionHandlerResolverWithJsonOutput();
    }

    @Bean
    public HttpMessageConverter converter() {
        return new MappingJackson2HttpMessageConverter();
    }

}
