package com.nexus.maven.netty.socket;


/**
 * Created by @panyao on 2017/8/25.
 */
public interface ActorWithApiRemoteHandler extends ActorWithApiHandler {

    String getRemoteActorPath();

}
