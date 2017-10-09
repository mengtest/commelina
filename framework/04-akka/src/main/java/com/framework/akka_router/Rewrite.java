package com.framework.akka_router;

import com.framework.message.ApiRequest;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/27.
 */
public interface Rewrite {

    Internal.EnumLite selectActorSeed(ApiRequest apiRequest);

    Internal.EnumLite selectActorSeed(ApiRequestForwardEntity requestForward);

}