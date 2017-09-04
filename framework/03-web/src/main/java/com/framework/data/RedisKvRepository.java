package com.framework.data;

/**
 * Created by @panyao on 2017/9/4.
 */
public interface RedisKvRepository {

    void put(String k, Integer v, Long pTtl);

    int getAsInt(String k);

}
