package com.commelina.akka.dispatching;

import com.commelina.akka.dispatching.local.AbstractLocalServiceActor;

/**
 *
 * @author @panyao
 * @date 2017/9/26
 */
public interface LocalServiceHandler extends Router {

    /**
     * 获取 context 的 actor props
     *
     * @return
     */
    Class<? extends AbstractLocalServiceActor> getPropsClass();

}