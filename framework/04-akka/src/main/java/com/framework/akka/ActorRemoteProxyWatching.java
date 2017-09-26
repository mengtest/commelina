package com.framework.akka;

import com.framework.message.ApiRequest;
import com.framework.message.ResponseMessageDomain;

/**
 * Created by @panyao on 2017/9/6.
 */
 interface ActorRemoteProxyWatching {

    void onRequest(ApiRequest request);

    void reply(ResponseMessageDomain message);

}
