package com.commelina.akka;

import com.commelina.akka.local.AbstractLocalServiceActor;

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