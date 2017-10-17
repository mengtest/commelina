package com.framework.akka.router;

import com.framework.akka.router.proto.ApiRequestForward;
import com.framework.niosocket.proto.SocketASK;

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
    int selectActorSeed(SocketASK ask);

    /**
     * server 重定向请求进行集群分发
     *
     * @param forward
     * @return
     */
    int selectActorSeed(ApiRequestForward forward);

}