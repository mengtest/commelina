package com.framework.akka_router;

import com.framework.niosocket.proto.SocketASK;

/**
 * Created by @panyao on 2017/9/25.
 */
public interface Dispatch {

    void onRequest(SocketASK request);

}