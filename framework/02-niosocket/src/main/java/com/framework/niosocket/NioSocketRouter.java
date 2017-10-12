package com.framework.niosocket;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by @panyao on 2017/8/25.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface NioSocketRouter {

    int forward();

}
