package com.app.passport.service;

import com.app.passport.entity.MemberEntity;
import com.framework.utils.ServiceDomainEmptyMessage;
import com.framework.utils.ServiceDomainMessage;

/**
 *
 * @author panyao
 * @date 2017/9/2
 */
public interface AccountService {

    /**
     * 手机号无密码登录/根据验证码登录
     * @param tel
     * @return
     */
    ServiceDomainMessage<MemberEntity> singInWithTelAndNoPassword(String tel);

    /**
     * 用手机号注册
     * @param tel
     * @param pwd
     * @return
     */
    ServiceDomainEmptyMessage registerTel(String tel, String pwd);

//    ServiceDomainMessage<MemberEntity> signInWithTel(String tel, String pwd);

//    MemberEntity registerEmail(String tel, String pwd);

//    MemberEntity singInWithEmail(String tel, String pwd);

}
