package com.framework.akka_router;

import akka.actor.Props;

/**
 * Created by @panyao on 2017/9/26.
 */
public interface ServiceHandler extends Router {

    Props getProps();

}