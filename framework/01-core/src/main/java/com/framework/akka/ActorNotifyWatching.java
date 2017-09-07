package com.framework.akka;

import com.framework.message.ApiRequest;

/**
 * Created by @panyao on 2017/9/6.
 */
public interface ActorNotifyWatching {

    void onNotify(ApiRequest request);

}
