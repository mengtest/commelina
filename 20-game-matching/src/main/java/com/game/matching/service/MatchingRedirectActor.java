package com.game.matching.service;

import akka.actor.UntypedActor;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by @panyao on 2017/8/14.
 *
 * 匹配重定向到房间的操作
 *
 */
public class MatchingRedirectActor extends UntypedActor {

    @Override
    public void onReceive(Object o) throws Throwable {
        if (o instanceof long[]) {
            long[] userIds = (long[]) o;
            // 成功了就关闭此次的重定向 actor
            if (this.createRoom(userIds)) {
                getContext().stop(getSelf());
                return;
            } else {
                // 失败了就把元素投递回去 MatchingActor 队列
                getSender().tell(userIds, getSelf());
                // FIXME: 2017/8/14 面临死循环问题
            }
            // 失败的重新投递回去，就关闭此次的 actor
        } else if (o == MatchingActor.MSG.MATCHING_REDIRECT_FAILED) {
            getContext().stop(getSelf());
        } else {
            this.unhandled(o);
        }
    }

    private boolean createRoom(long[] userIds) {
        // 创建定时器 10s 不成功，则认为失败了
        getContext().system().scheduler().scheduleOnce(Duration.create(10000, TimeUnit.MILLISECONDS), () -> {
            // 停止当前 actor
            getContext().stop(getSelf());
        }, getContext().system().dispatcher());


        return false;
    }

}
