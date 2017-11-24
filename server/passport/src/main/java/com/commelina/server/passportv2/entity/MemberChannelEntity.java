package com.commelina.server.passportv2.entity;

import javax.persistence.*;

/**
 * @author panyao
 * @date 2017/11/21
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "member_channel")
public class MemberChannelEntity {

    /**
     * 主键ID
     */
    @Id
    @Column
    private long mcid;

    /**
     * 产品id
     */
    @Column
    private long appid;

    /**
     * 渠道id
     */
    @Column
    private Channel channel;

    /**
     * 用户 uid
     */
    @Column
    private long uid;

    /**
     * 主帐户 uid
     */
    @Column
    private long mainUid;

}