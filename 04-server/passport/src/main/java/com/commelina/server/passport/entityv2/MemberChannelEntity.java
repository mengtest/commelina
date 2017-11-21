package com.commelina.server.passport.entityv2;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @author panyao
 * @date 2017/11/21
 */
public class MemberChannelEntity {

    /**
     * 主键ID
     */
    @Id
    private long mcid;
    /**
     * 产品id
     */
    @Column
    private long appid;
    /**
     * 渠道id
     */
    private Channel channel;
    /**
     * 用户 uid
     */
    private long uid;
    /**
     * 主帐户 uid
     */
    private long mainUid;

}