package com.commelina.server.passportv2.entity;

import javax.persistence.*;

/**
 * 用户帐户汇总
 *
 * @author panyao
 * @date 2017/11/21
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "member_account")
public class MemberAccountEntity {

    @Id
    @Column(updatable = false, nullable = false)
    private long mcid;

    @Column(updatable = false, nullable = false)
    private AccountType accountType;

    @Column(updatable = false, nullable = false)
    private String account;

    @Column(updatable = false, nullable = false)
    private long uid;

}