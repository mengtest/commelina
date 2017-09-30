package com.framework.akka_router.cluster;

import akka.util.Timeout;
import com.framework.message.ApiRequestForward;
import com.google.protobuf.Internal;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by @panyao on 2017/9/25.
 */
public class ClusterChildNodeSystem {

    public static final Timeout defaultTimeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    public Future<Object> askForward(Internal.EnumLite forwardId, ApiRequestForward requestForward) {
        return null;
    }

}