package com.commelina.sangong.context;

import com.commelina.sangong.Behavior;
import com.commelina.sangong.Controller;
import com.commelina.sangong.MemberEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by panyao on 2017/12/2.
 */
public class Room implements MemberEvent {

    // 维护当前的 阶段
    private Controller currentController;

    private Map<Integer, Behavior> behaviors = new HashMap<Integer, Behavior>();

    private final Lock behaviorExecuterLock = new ReentrantLock();

    public void onRequest(ChannelHandlerContext context, int userId) {
        // 分配到对于的 behavior 上去
    }

    public void changeToNextController(Controller controller) {
        currentController = controller;
        int waitTime = controller.onStart(this);
        if (waitTime > 0) {
            // 用计时器超时切换阶段 进行 onUpdate
            addControllerExpireEvent(waitTime);
        } else {
            controllerUpdate();
        }
    }

    private void controllerUpdate() {
        currentController.onUpdate(this);
        if (currentController.isOver(this)) {
            currentController.onCompleted(this);
        }
    }

    public void addBehavior(int userId, Behavior behavior) {
        // 加入执行队列
        int waitTime = behavior.onStart(userId, this);
        if (waitTime > 0) {
            addBehaviorExpireEvent(waitTime, userId, behavior);
        }
    }

    // 加入延迟器
    private void addControllerExpireEvent(int waitTime) {
        // todo 计时器超时 执行

    }

    private void addBehaviorExpireEvent(int waitTime, int userId, Behavior behavior) {
        // 计时器超时则 执行

    }

}


