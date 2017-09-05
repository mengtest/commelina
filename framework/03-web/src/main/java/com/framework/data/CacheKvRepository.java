package com.framework.data;

/**
 * Created by @panyao on 2017/9/4.
 */
public interface CacheKvRepository {

    void put(String k, Long v, Long pTtl);

    void remove(String k);

    boolean expire(String k, Long pTtl);

    long getAsLong(String k);

    String getAsString(String k);

}
