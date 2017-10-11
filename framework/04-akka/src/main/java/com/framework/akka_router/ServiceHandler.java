package com.framework.akka_router;

import com.framework.akka_router.local.AbstractLocalServiceActor;

/**
 * Created by @panyao on 2017/9/26.
 */
public interface ServiceHandler extends Router {

    Class<? extends AbstractLocalServiceActor> getPropsClass();

}