package com.app.passport.entity;

import javax.persistence.*;

/**
 *
 * @author panyao
 * @date 2017/9/2
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "passport_account")
public class AccountEntity {

    @Id
    @Column(updatable = false, nullable = false)
    private long uid;

    @Column(length = 80, updatable = false, nullable = false)
    private String account;

    @Column(updatable = false, nullable = false)
    @Enumerated
    private ACCOUNT_TYPE type;

    public enum ACCOUNT_TYPE {
        TELEPHONE, EMAIL
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
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
