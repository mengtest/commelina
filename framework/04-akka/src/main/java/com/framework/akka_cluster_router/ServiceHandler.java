package com.framework.akka_cluster_router;

import akka.actor.Props;
import com.framework.message.ApiRequestLogin;

/**
 * Created by @panyao on 2017/9/26.
 */
public interface ServiceHandler extends Router {

    Props getProps();

    void onRequest(ApiRequestLogin request);

}
