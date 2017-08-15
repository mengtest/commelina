package com.nexus.maven.netty.socket;

import com.nexus.maven.netty.socket.router.ResponseHandler;

/**
 * Created by @panyao on 2017/8/11.
 */
public interface NotifyResponseHandler extends ResponseHandler {

    long getUserId();

    PipelineNotifyFuture getListener();

}
