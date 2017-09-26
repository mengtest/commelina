package com.framework.akka_cluster_router;

import com.framework.message.ApiRequestLogin;

/**
 * Created by @panyao on 2017/9/25.
 */
public interface Dispatch {

    void onRequest(ApiRequestLogin request);

}
