package com.framework.niosocket;

import org.springframework.stereotype.Component;

/**
 * Created by @panyao on 2017/8/25.
 */
@Component
public @interface NioSocketRouter {

    int forward();

}
