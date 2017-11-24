package com.commelina.math24.play.robot;

import com.commelina.math24.play.robot.interfaces.MemberEvent;
import com.commelina.math24.play.robot.niosocket.MemberEventLoop;
import com.commelina.math24.play.robot.niosocket.NettyClient;

/**
 * @author panyao
 * @date 2017/11/23
 */
public class Starter {

    public static void start(String account, String pwd, MemberEvent firstEvent) throws InterruptedException {
        MemberEventLoop eventLoop = new MemberEventLoop(() -> {
            // request token by account and pwd.
            return "xx";
        }, firstEvent);

        NettyClient.start(eventLoop);
    }

}
