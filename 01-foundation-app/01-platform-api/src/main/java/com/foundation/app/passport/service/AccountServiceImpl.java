package com.foundation.app.passport.service;

import com.foundation.app.passport.dao.AccountRepository;
import com.foundation.app.passport.dao.MemberRepository;
import com.foundation.app.passport.entity.AccountEntity;
import com.foundation.app.passport.entity.MemberEntity;
import com.foundation.game_gateway.proto.ERROR_CODE_CONSTANTS;
import com.framework.core.ServiceDomainMessage;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * Created by panyao on 2017/9/2.
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private MemberRepository memberRepository;

    @Override
    public ServiceDomainMessage<MemberEntity> registerTelWithNoPassword(String tel) {
        return registerTel(tel, tel);
    }

    @Override
    public ServiceDomainMessage<MemberEntity> singInWithTelAndNoPassword(String tel) {
        return singInWithTel(tel, tel);
    }

    @Transactional
    // 这里只处理 unique 异常
    @Override
    public ServiceDomainMessage<MemberEntity> registerTel(String tel, String pwd) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public ServiceDomainMessage<MemberEntity> singInWithTel(@NotNull String tel, @NotNull String pwd) {
        AccountEntity entity = accountRepository.findByAcountAndType(tel, AccountEntity.ACCOUNT_TYPE.TELEPHONE);
        if (entity == null) {
            return ServiceDomainMessage.newMessage(ERROR_CODE_CONSTANTS.ACCOUNT_NOT_FOUND);
        }

        MemberEntity memberEntity = memberRepository.findOne(entity.getUserId());
        if (memberEntity == null) {
            return ServiceDomainMessage.newMessage(ERROR_CODE_CONSTANTS.ACCOUNT_MEMBER_NOT_FOUND);
        }

        String createHashPwd = Hashing.sha256().hashUnencodedChars(pwd).toString();
        if (!createHashPwd.equals(memberEntity.getPwd())) {
            return ServiceDomainMessage.newMessage(ERROR_CODE_CONSTANTS.PASSWORD_VALID_ERROR);
        }

        return ServiceDomainMessage.newMessage(memberEntity);
    }

}
