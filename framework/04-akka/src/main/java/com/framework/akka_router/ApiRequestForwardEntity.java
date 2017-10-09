package com.framework.akka_router;

import com.framework.message.ApiRequestForward;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/15.
 */
public class ApiRequestForwardEntity {

    private final Internal.EnumLite forwardId;

    private final ApiRequestForward requestForward;

    private ApiRequestForwardEntity(Internal.EnumLite forwardId, ApiRequestForward requestForward) {
        this.forwardId = forwardId;
        this.requestForward = requestForward;
    }

    public static ApiRequestForwardEntity newRequest(Internal.EnumLite forwardId, ApiRequestForward requestForward) {
        return new ApiRequestForwardEntity(forwardId, requestForward);
    }

    public Internal.EnumLite getForwardId() {
        return forwardId;
    }

    public ApiRequestForward getRequestForward() {
        return requestForward;
    }

}