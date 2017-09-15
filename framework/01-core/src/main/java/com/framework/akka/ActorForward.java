package com.framework.akka;

import com.framework.message.ResponseForward;

/**
 * Created by @panyao on 2017/9/15.
 */
interface ActorForward {

    void onForwardEvent(ResponseForward forward);

}
