package com.framework.akka;

import com.framework.message.ApiRequest;
import com.framework.message.ResponseMessage;

/**
 * Created by @panyao on 2017/9/6.
 */
public interface ActorRequestWatching {

    void onRequest(ApiRequest request);

    void reply(ResponseMessage message);

}
