package com.github.freedompy.commelina.akka.router;

import com.github.freedompy.commelina.akka.router.proto.ApiRequest;

/**
 * @author @panyao
 * @date 2017/9/25
 */
public interface Dispatch {

    /**
     * 当有新的请求到来时触发
     *
     * @param request
     */
    void onRequest(ApiRequest request);

}