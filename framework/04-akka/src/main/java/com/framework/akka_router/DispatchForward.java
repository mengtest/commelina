package com.framework.akka_router;

/**
 * Created by @panyao on 2017/9/30.
 */
public interface DispatchForward extends Dispatch {

    void onForward(ApiRequestForward forward);

}