package com.foundation.game_matching;

/**
 * Created by @panyao on 2017/8/31.
 */
public final class MatchingConfigEntity {

    private final int matchSuccessPeople;

    MatchingConfigEntity(int matchSuccessPeople) {
        this.matchSuccessPeople = matchSuccessPeople;
    }

    public int getMatchSuccessPeople() {
        return matchSuccessPeople;
    }
}
