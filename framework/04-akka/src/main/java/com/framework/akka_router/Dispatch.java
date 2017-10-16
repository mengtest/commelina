package com.framework.akka_router;

/**
 * Created by @panyao on 2017/9/25.
 */
public interface Dispatch {

    void onRequest(ApiRequest request);

}