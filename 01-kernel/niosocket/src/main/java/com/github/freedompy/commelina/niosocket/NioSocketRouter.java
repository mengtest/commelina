package com.github.freedompy.commelina.niosocket;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * socket 路由 注解，根据这个获取对应的beans 加入到 server routers 中
 *
 * @author @panyao
 * @date 2017/8/25
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface NioSocketRouter {

    int forward();

}
