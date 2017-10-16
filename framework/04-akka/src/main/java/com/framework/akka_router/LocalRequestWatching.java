package com.framework.akka_router;

import com.framework.niosocket.proto.SocketASK;

/**
 * Created by @panyao on 2017/10/16.
 */
public interface LocalRequestWatching {

    void onRequest(SocketASK request);

}
