package com.commelina.sangong.context;

import com.commelina.sangong.Behavior;
import com.commelina.sangong.Controller;
import com.commelina.sangong.MemberEvent;

/**
 * Created by panyao on 2017/12/2.
 */
public class Room implements MemberEvent {

    // 维护当前的 阶段
    private Controller currentController;

    public void onRequest(ChannelHandlerContext context, int userId) {
        // 分配到对于的 behavior 上去
    }

    public void addController(Controller controller) {
        int waitTime = controller.onStart(this);
        if (waitTime > 0) {
            addExpireEvent();
        } else {
            addExecute();
        }

        // 处理延迟

        // 加入执行队列
    }

    public void addBehavior(int userId, Behavior behavior) {
        // 加入执行队列
    }

    public void onTimer() {
        //
        // 计时器超时触发之后，转化成 behavior 或者 controller addExecute
        // 不要直接修改当前 room 的值
    }

    // 加入延迟器
    private void addExpireEvent() {

    }

    // 加入执行队列
    private void addExecute() {

    }

    private void execute(){
        // 队列等待 behavior
        Object o = null;
    }

}
