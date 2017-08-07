package com.game.framework.netty.protocol;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by @panyao on 2017/8/7.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface JSONRpc {

}
