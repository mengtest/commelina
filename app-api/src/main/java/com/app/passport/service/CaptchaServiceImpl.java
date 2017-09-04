package com.app.passport.service;

import com.framework.utils.RandomEnhanced;
import com.framework.data.RedisKvRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by @panyao on 2017/9/4.
 */
@Repository
public class CaptchaServiceImpl implements CaptchaService {

    @Resource
    private RedisKvRepository kvRepository;

    private final RandomEnhanced telephoneRandom = new RandomEnhanced();

    @Override
    public void telephoneSms(String tel) {
        final int code = telephoneRandom.nextInt(1000, 9999);

        // 发送短信
    }

    @Override
    public void validTelephoneCode(String tel, String code) {

    }
}
