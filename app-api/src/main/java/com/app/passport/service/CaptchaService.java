package com.app.passport.service;

/**
 * Created by @panyao on 2017/9/4.
 */
public interface CaptchaService {

    void telephoneSms(String tel);

    void validTelephoneCode(String tel, String code);

}
