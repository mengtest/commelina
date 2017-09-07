package com.framework.niosocket;

import org.springframework.stereotype.Component;

/**
 * Created by @panyao on 2017/8/25.
 *
 * 代理请求的 actor
 *
 */
@Component
public @interface ActorRequestController {

    int apiPathCode();

}
