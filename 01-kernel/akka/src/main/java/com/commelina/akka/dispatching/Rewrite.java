package com.commelina.akka.dispatching;

import akka.actor.ActorRef;
import com.commelina.akka.dispatching.proto.ApiRequest;
import com.commelina.akka.dispatching.proto.ApiRequestForward;
import com.google.common.collect.BiMap;

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
    ActorRef selectActor(ApiRequest ask, BiMap<Integer, ActorRef> clusterNodeRouters);

    /**
     * server 重定向请求进行集群分发
     *
     * @param forward
     * @return
     */
    ActorRef selectActor(ApiRequestForward forward, BiMap<Integer, ActorRef> clusterNodeRouters);

}