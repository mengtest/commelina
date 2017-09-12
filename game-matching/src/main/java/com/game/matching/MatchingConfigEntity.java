package com.game.matching;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by @panyao on 2017/8/31.
 */
@Component
@ConfigurationProperties(prefix = "matching")
public final class MatchingConfigEntity {

    private int queueSuccessPeople;
    private int queueSizeRate;

    public int getQueueSuccessPeople() {
        return queueSuccessPeople;
    }

    public int getQueueSizeRate() {
        return queueSizeRate;
    }

    public void setQueueSuccessPeople(int queueSuccessPeople) {
        this.queueSuccessPeople = queueSuccessPeople;
    }

    public void setQueueSizeRate(int queueSizeRate) {
        this.queueSizeRate = queueSizeRate;
    }
}
