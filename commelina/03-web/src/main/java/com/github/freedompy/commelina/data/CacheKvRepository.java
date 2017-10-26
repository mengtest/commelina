package com.github.freedompy.commelina.data;

/**
 *
 * @author @panyao
 * @date 2017/9/4
 */
public interface CacheKvRepository {

    void put(String k, long v, Long pTtl);

    void put(String k, int v, Long pTtl);

    long getAndSet(String k, Long v, Long pTtl);

    void remove(String k);

    long getAsLong(String k);

    int getAsInt(String k);

    String getAsString(String k);

}
