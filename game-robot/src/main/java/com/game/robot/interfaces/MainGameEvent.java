package com.game.robot.interfaces;

import java.util.List;

/**
 * Created by @panyao on 2017/9/11.
 */
public interface MainGameEvent extends SocketHandler {

    void start(HandlerEvent event, HandlerEvent... events);

    void start(List<HandlerEvent> events);

}
