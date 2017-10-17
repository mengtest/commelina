package com.framework.akka.router;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author @panyao
 * @date 2017/10/1
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ActorServiceHandler {
}
