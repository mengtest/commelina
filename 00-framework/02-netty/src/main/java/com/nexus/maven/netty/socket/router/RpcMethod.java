package com.nexus.maven.netty.socket.router;

import java.lang.annotation.*;

/**
 * Created by @panyao on 2017/8/7.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcMethod {

    String value();

    String version() default Version.FIRST_VERSION;

}
