package com.github.freedompy.commelina.akka.router;

import com.github.freedompy.commelina.akka.router.proto.ApiRequest;
import com.github.freedompy.commelina.akka.router.proto.ApiRequestForward;

/**
 * @author @panyao
 * @date 2017/9/27
 */
public interface Rewrite {

    /**
     * 客户端请求进行集群负载
     *
     * @param ask
     * @return
     */
    int selectActorSeed(ApiRequest ask);

    /**
     * server 重定向请求进行集群分发
     *
     * @param forward
     * @return
     */
    int selectActorSeed(ApiRequestForward forward);

}