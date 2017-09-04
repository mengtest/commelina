package com.game.matching;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by @panyao on 2017/8/31.
 */
@ConfigurationProperties(prefix = "matching")
public class MatchingConfigEntity {

    private int queueSucessPeople;
    private int queueSizeRate;

    public int getQueueSuccessPeople() {
        return queueSucessPeople;
    }

    public int getQueueSizeRate() {
        return queueSizeRate;
    }

    public int getQueueSucessPeople() {
        return queueSucessPeople;
    }

    public void setQueueSucessPeople(int queueSucessPeople) {
        this.queueSucessPeople = queueSucessPeople;
    }

    public void setQueueSizeRate(int queueSizeRate) {
        this.queueSizeRate = queueSizeRate;
    }
}
