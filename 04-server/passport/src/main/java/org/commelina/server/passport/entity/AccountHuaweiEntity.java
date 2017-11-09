package org.commelina.server.passport.entity;

import javax.persistence.*;

/**
 *
 * @author panyao
 * @date 2017/9/2
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "passport_account_huawei")
public class AccountHuaweiEntity {

    @Id
    @Column(updatable = false, nullable = false)
    private long uid;

    @Column(length = 80, updatable = false, nullable = false)
    private String account;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

}
