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
        stringRedisTemplate.opsForValue().set(k, v + "", pTtl, TimeUnit.MICROSECONDS);
    }

    @Override
    public int getAsInt(String k) {
        String val = stringRedisTemplate.opsForValue().get(k);
        if (Strings.isNullOrEmpty(val)) {
            return 0;
        }
        return Integer.valueOf(val);
    }

}
