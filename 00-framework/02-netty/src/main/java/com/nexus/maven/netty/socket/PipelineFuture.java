package com.nexus.maven.netty.socket;

import io.netty.channel.ChannelFuture;

/**
 * Created by @panyao on 2017/8/9.
 */
public interface PipelineFuture {

    void call(ChannelFuture future);

}
