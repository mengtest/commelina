package com.foundation.app.passport.entity;

/**
 * Created by panyao on 2017/9/2.
 */
public class AccountEntity {
    private long userId;
    private ACCOUNT_TYPE type;
    private String account;

    public enum ACCOUNT_TYPE {
        TELEPHONE, EMAIL
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public ACCOUNT_TYPE getType() {
        return type;
    }

    public void setType(ACCOUNT_TYPE type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
