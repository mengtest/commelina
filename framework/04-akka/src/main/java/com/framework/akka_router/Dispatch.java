package com.framework.akka_router;

import com.framework.message.ApiRequest;
import com.framework.message.ApiRequestForward;

/**
 * Created by @panyao on 2017/9/25.
 */
public interface Dispatch {

    void onRequest(ApiRequest request);

    void onForward(ApiRequestForward forward);

}