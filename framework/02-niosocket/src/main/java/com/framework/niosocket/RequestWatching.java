package com.framework.niosocket;

import com.framework.message.ApiRequest;

/**
 * Created by @panyao on 2017/9/6.
 */
public interface RequestWatching {

    void onRequest(ApiRequest request);

}
