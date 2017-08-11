package com.nexus.maven.netty.socket.router;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by @panyao on 2017/8/7.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RpcApi {

    String apiName() default "";

}
