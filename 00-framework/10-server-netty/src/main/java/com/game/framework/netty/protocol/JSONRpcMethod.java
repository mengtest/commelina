package com.game.framework.netty.protocol;

import java.lang.annotation.*;

/**
 * Created by @panyao on 2017/8/7.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JSONRpcMethod{
}
