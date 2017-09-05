package com.framework.data;

import com.google.common.base.Strings;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by @panyao on 2017/9/4.
 */
public class RedisCacheKvRepositoryImpl implements CacheKvRepository {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void put(String k, Long v, Long pTtl) {
        stringRedisTemplate.opsForValue().set("kv:" + k, v + "", pTtl, TimeUnit.MICROSECONDS);
    }

    @Override
    public void remove(String k) {
        stringRedisTemplate.delete("kv:" + k);
    }

    @Override
    public boolean expire(String k, Long pTtl) {
       return stringRedisTemplate.expire("kv:" + k, pTtl, TimeUnit.MICROSECONDS);
    }

    @Override
    public long getAsLong(String k) {
        String val = this.getAsString(k);
        if (Strings.isNullOrEmpty(val)) {
            return 0;
        }
        return Long.valueOf(val);
    }

    @Override
    public String getAsString(String k) {
        return stringRedisTemplate.opsForValue().get("kv:" + k);
    }

}
