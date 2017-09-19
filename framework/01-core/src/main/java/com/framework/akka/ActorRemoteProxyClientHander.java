package com.framework.akka;

import com.framework.message.ApiRequest;
import com.framework.message.ResponseMessage;

/**
 * Created by @panyao on 2017/9/15.
 */
public interface ActorRemoteProxyClientHander extends ActorRemoteProxyWatching {

    void onRequest(ApiRequest request);

    void reply(ResponseMessage message);

}
