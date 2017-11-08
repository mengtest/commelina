package com.commelina.example.robot.interfaces;

/**
 *
 * @author @panyao
 * @date 2017/9/11
 */
public interface MainGameEvent extends SocketHandler {

    void start(MemberEvent event, MemberEvent... events);

}
