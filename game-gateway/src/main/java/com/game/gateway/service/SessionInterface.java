package com.game.gateway.service;

/**
 * Created by @panyao on 2017/8/8.
 */
public interface SessionInterface {

    boolean valid(String token);

}