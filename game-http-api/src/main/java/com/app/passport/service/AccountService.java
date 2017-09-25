package com.app.passport.service;

import com.app.passport.entity.MemberEntity;
import com.framework.utils.ServiceDomainEmptyMessage;
import com.framework.utils.ServiceDomainMessage;

/**
 * Created by panyao on 2017/9/2.
 */
public interface AccountService {

    ServiceDomainMessage<MemberEntity> singInWithTelAndNoPassword(String tel);

    ServiceDomainEmptyMessage registerTel(String tel, String pwd);

    ServiceDomainMessage<MemberEntity> singInWithTel(String tel, String pwd);

//    MemberEntity registerEmail(String tel, String pwd);

//    MemberEntity singInWithEmail(String tel, String pwd);

}
