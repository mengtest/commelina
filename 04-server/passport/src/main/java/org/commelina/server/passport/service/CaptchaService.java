package org.commelina.server.passport.service;

/**
 * @author @panyao
 * @date 2017/9/4
 */
public interface CaptchaService {

    /**
     * 发送验证短信
     *
     * @param tel
     */
    void telephoneSms(String tel);

    /**
     * 验证手机验证码
     *
     * @param tel
     * @param code
     * @return
     */
    boolean validTelephoneCode(String tel, int code);

}
