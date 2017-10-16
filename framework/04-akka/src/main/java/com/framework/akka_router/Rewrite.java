package com.framework.akka_router;

import com.framework.niosocket.proto.SocketASK;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/27.
 */
public interface Rewrite {

    Internal.EnumLite selectActorSeed(SocketASK ask);
    Internal.EnumLite selectActorSeed(ApiRequestForward forward);

}