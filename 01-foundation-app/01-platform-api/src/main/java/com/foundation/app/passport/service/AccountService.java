package com.foundation.app.passport.service;

import com.foundation.app.passport.entity.MemberEntity;
import com.framework.core.ServiceDomainMessage;

/**
 * Created by panyao on 2017/9/2.
 */
public interface AccountService {

    ServiceDomainMessage<MemberEntity> registerTelWithNoPassword(String tel);

    ServiceDomainMessage<MemberEntity> singInWithTelAndNoPassword(String tel);

    ServiceDomainMessage<MemberEntity> registerTel(String tel, String pwd);

    ServiceDomainMessage<MemberEntity> singInWithTel(String tel, String pwd);

//    MemberEntity registerEmail(String tel, String pwd);
//
//    MemberEntity singInWithEmail(String tel, String pwd);

}
