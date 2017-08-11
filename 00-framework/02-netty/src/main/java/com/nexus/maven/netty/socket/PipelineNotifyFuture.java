package com.nexus.maven.netty.socket;

import io.netty.channel.ChannelFuture;

/**
 * Created by @panyao on 2017/8/9.
 */
public interface PipelineNotifyFuture extends PipelineFuture {

    void call(STATUS_CODE code, ChannelFuture future);

    enum STATUS_CODE {
        SUCCESS, USER_UN_LOGIN, USER_CHANNEL_UN_ACTIVE, UNDEFIEND_ERRROR
    }

}
