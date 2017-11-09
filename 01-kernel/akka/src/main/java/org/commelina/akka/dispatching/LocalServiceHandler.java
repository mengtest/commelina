package org.commelina.akka.dispatching;

import org.commelina.akka.dispatching.local.AbstractLocalServiceActor;

/**
 *
 * @author @panyao
 * @date 2017/9/26
 */
public interface LocalServiceHandler extends Router {

    /**
     * 获取 service 的 actor props
     *
     * @return
     */
    Class<? extends AbstractLocalServiceActor> getPropsClass();

}