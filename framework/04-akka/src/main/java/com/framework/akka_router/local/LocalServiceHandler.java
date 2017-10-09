package com.framework.akka_router.local;

import com.framework.akka_router.Router;

/**
 * Created by @panyao on 2017/9/26.
 */
public interface LocalServiceHandler extends Router {

    Class<? extends AbstractServiceActor> getPropsClass();

}