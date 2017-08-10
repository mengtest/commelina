package com.nexus.maven.netty.router;

import io.netty.channel.ChannelFuture;

/**
 * Created by @panyao on 2017/8/9.
 */
public interface PipelineFuture {

    void call(ChannelFuture future);

}
