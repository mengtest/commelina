package com.framework.data;

import com.google.common.base.Strings;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by @panyao on 2017/9/4.
 */
@Component
public class RedisKvRepositoryImpl implements RedisKvRepository {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void put(String k, Integer v, Long pTtl) {
        stringRedisTemplate.opsForValue().set("kv:" + k, v + "", pTtl, TimeUnit.MICROSECONDS);
    }

    @Override
    public void remove(String k) {
        stringRedisTemplate.delete("kv:" + k);
    }

    @Override
    public int getAsInt(String k) {
        String val = this.getAsString(k);
        if (Strings.isNullOrEmpty(val)) {
            return 0;
        }
        return Integer.valueOf(val);
    }

    @Override
    public String getAsString(String k) {
        return stringRedisTemplate.opsForValue().get("kv:" + k);
    }

}
