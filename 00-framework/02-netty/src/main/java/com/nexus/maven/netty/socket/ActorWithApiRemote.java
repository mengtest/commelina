package com.nexus.maven.netty.socket;

import akka.actor.Props;

/**
 * Created by @panyao on 2017/8/25.
 */
public interface ActorWithApiRemote {

    // 接口名字
    String getApiName();

    // actor creator
    Props getProps(ActorResponseContext context);

}
