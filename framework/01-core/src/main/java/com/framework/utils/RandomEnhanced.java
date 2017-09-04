package com.framework.utils;

import com.google.common.base.Preconditions;

import java.util.Random;

/**
 * Created by @panyao on 2017/9/4.
 */
public final class RandomEnhanced extends Random {

    /**
     * 按照范围取随机数
     */
    public int nextInt(int min, int max) {
        Preconditions.checkArgument(max > min);
        return nextInt(max) % (max - min + 1) + min;
    }

}
