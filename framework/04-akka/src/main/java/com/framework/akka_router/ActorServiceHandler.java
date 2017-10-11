package com.framework.akka_router;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by @panyao on 2017/10/11.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ActorServiceHandler {
}
