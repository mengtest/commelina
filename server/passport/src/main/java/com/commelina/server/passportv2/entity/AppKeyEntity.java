package com.commelina.server.passportv2.entity;

import javax.persistence.*;

/**
 * @author panyao
 * @date 2017/11/21
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "app_key")
public class AppKeyEntity {

    @Id
    @Column(updatable = false, nullable = false)
    private long appId;

    @Column(length = 80, updatable = false, nullable = false)
    private String appSecretKey;
}
