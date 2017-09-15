package com.framework.akka;

import com.framework.message.ApiRequestForward;

/**
 * Created by @panyao on 2017/9/15.
 */
interface ActorReceiveForward {

    void onForwardEvent(ApiRequestForward forward);

}
