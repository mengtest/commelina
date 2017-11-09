package org.commelina.akka.dispatching;

import org.commelina.akka.dispatching.proto.ApiRequestForward;

/**
 *
 * @author @panyao
 * @date 2017/9/30
 */
public interface DispatchForward extends Dispatch {

    /**
     * 当有新的 server 重定向消息时触发
     * @param forward
     */
    void onForward(ApiRequestForward forward);

}