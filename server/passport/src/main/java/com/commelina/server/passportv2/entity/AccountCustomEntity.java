package com.commelina.server.passportv2.entity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * 用户自定义账号表
 *
 * @author panyao
 * @date 2017/11/21
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "member_channel_custom")
public class AccountCustomEntity {

    private long uid;

    private String account;

}
