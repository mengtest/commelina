package com.game.matching;

import com.framework.akka_router.cluster.node.ClusterChildNodeBackedActor;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/26.
 */
public class MatchingRouter extends ClusterChildNodeBackedActor {

    @Override
    public Internal.EnumLite getRouterId() {
        // 这里就是配置节点，节点 1 节点 2 节点 3，不会因为集群的启动顺序而改变
        return () -> 0;
    }

}