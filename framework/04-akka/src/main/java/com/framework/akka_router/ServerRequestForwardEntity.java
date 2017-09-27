package com.framework.akka_router;

import com.framework.message.ApiRequestForward;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/27.
 */
public class ServerRequestForwardEntity {

    private final Internal.EnumLite forwardId;
    private final ApiRequestForward requestForward;

    public ServerRequestForwardEntity(Internal.EnumLite forwardId, ApiRequestForward requestForward) {
        this.forwardId = forwardId;
        this.requestForward = requestForward;
    }

    public Internal.EnumLite getForwardId() {
        return forwardId;
    }

    public ApiRequestForward getRequestForward() {
        return requestForward;
    }

}
