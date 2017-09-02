package com.foundation.app.passport.entity;

/**
 * Created by panyao on 2017/9/2.
 */
public class MemberEntity {

    private long userId;
    private String pwd;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}