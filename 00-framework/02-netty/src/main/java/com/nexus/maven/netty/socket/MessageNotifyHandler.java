package com.nexus.maven.netty.socket;

/**
 * Created by @panyao on 2017/8/11.
 */
public interface MessageNotifyHandler extends MessageHandler {

    long getUserId();

    PipelineNotifyFuture getListener();

}
