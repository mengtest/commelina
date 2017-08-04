package com.game.framework.netty;

/**
 * Created by @panyao on 2017/8/3.
 */
public interface SessionInterface {

    /**
     * 验证用户的 token 是否合法
     *
     * @param token
     * @return true 合法
     */
    boolean valid(String token);

}
