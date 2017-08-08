package com.game.foundation.netty;

/**
 * Created by @panyao on 2017/8/7.
 */
public class MessageEntity<T> {

    private long userId;
    private T msg;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public T getMsg() {
        return msg;
    }

    public void setMsg(T msg) {
        this.msg = msg;
    }
}
