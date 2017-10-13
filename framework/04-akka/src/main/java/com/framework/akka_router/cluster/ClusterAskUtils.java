package com.framework.akka_router.cluster;

import akka.util.Timeout;
import com.framework.message.ApiRequest;
import com.framework.message.MessageBus;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/10/9.
 *
 * 工具类，用于给分布式的节点上的 server 发送消息
 */
public final class ClusterAskUtils {

    /**
     * 要请求的 server 的名字，要请求的接口
     * @param domainRouterId
     * @param apiRequest
     * @return
     */
    public static MessageBus askRouterClusterNode(Internal.EnumLite domainRouterId, ApiRequest apiRequest) {
        return askRouterClusterNode(domainRouterId, apiRequest, AkkaMultiWorkerSystem.DEFAULT_TIMEOUT);
    }

    public static MessageBus askRouterClusterNode(Internal.EnumLite domainRouterId, ApiRequest apiRequest, Timeout timeout) {
        return AkkaMultiWorkerSystemContext.INSTANCE.getContext(domainRouterId).askRouterClusterNode(apiRequest, timeout);
    }

}