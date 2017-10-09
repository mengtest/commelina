package com.framework.akka_router.cluster;

import akka.util.Timeout;
import com.framework.message.ApiRequest;
import com.framework.message.ApiRequestForward;
import com.google.protobuf.Internal;
import scala.concurrent.Future;

/**
 * Created by @panyao on 2017/10/9.
 */
public final class ClusterAskUtils {

    public static Future<Object> askRouterClusterNode(Internal.EnumLite routerId, ApiRequest apiRequest) {
        return askRouterClusterNode(routerId, apiRequest, AkkaMultiWorkerSystemV3.DEFAULT_TIMEOUT);
    }

    public static Future<Object> askRouterClusterNode(Internal.EnumLite routerId, ApiRequest apiRequest, Timeout timeout) {
        return AkkaMultiWorkerSystemContext.INSTANCE.getContext(routerId).askRouterClusterNode(apiRequest, timeout);
    }

    public static Future<Object> askRouterClusterNodeForward(Internal.EnumLite routerId, ApiRequestForward apiRequest) {
        return askRouterClusterNodeForward(routerId, apiRequest, AkkaMultiWorkerSystemV3.DEFAULT_TIMEOUT);
    }

    public static Future<Object> askRouterClusterNodeForward(Internal.EnumLite routerId, ApiRequestForward apiRequest, Timeout timeout) {
        return AkkaMultiWorkerSystemContext.INSTANCE.getContext(routerId).askRouterClusterNode(apiRequest, timeout);
    }

}