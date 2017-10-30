package com.business.service.uc.entity;

import javax.persistence.*;

/**
 *
 * @author panyao
 * @date 2017/9/2
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "passport_member")
public class MemberEntity {

    @Id
    @Column(updatable = false, nullable = false)
    private long uid;

    @Column(length = 60, nullable = false)
    private String pwd;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

}