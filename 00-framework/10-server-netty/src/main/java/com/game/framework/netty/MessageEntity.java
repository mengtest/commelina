package com.game.framework.netty;

/**
 * Created by @panyao on 2017/8/7.
 */
public class MessageEntity {

    private long userId;
    private Object msg;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }
}
