package com.framework.akka;

import com.framework.message.ApiRequestLogin;
import com.framework.message.ResponseMessageDomain;

/**
 * Created by @panyao on 2017/9/6.
 */
 interface ActorRemoteProxyWatching {

    void onRequest(ApiRequestLogin request);

    void reply(ResponseMessageDomain message);

}
