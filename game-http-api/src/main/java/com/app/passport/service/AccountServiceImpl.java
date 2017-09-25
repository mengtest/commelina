package com.app.passport.service;

import com.app.passport.dao.AccountRepository;
import com.app.passport.dao.MemberRepository;
import com.app.passport.entity.AccountEntity;
import com.app.passport.entity.MemberEntity;
import com.app.passport.proto.ERROR_CODE_CONSTANTS;
import com.framework.utils.ServiceDomainEmptyMessage;
import com.framework.utils.ServiceDomainMessage;
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

    @Transactional
    @Override
    public ServiceDomainMessage<MemberEntity> singInWithTelAndNoPassword(String tel) {
        AccountEntity entity = accountRepository.findByAccountAndType(tel, AccountEntity.ACCOUNT_TYPE.TELEPHONE);
        if (entity != null) {
            MemberEntity memberEntity = memberRepository.findOne(entity.getUid());
            if (memberEntity == null) {
                return ServiceDomainMessage.newMessage(ERROR_CODE_CONSTANTS.ACCOUNT_MEMBER_NOT_FOUND);
            }
            return ServiceDomainMessage.newMessage(memberEntity);
        }

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setPwd(PWDUtils.createPwd(tel));
        memberEntity = memberRepository.save(memberEntity);

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setUid(memberEntity.getUid());
        accountEntity.setType(AccountEntity.ACCOUNT_TYPE.TELEPHONE);
        accountEntity.setAccount(tel);

        // FIXME: 2017/9/4 这里要处理唯一索引问题
        accountRepository.save(accountEntity);

        return ServiceDomainMessage.newMessage(memberEntity);
    }

    @Transactional
    // 这里只处理 unique 异常
    @Override
    public ServiceDomainEmptyMessage registerTel(@NotNull String tel, @NotNull String pwd) {
        AccountEntity entity = accountRepository.findByAccountAndType(tel, AccountEntity.ACCOUNT_TYPE.TELEPHONE);
        if (entity != null) {
            return ServiceDomainEmptyMessage.newMessage(ERROR_CODE_CONSTANTS.ACCOUNT_EXISTS);
        }
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setPwd(PWDUtils.createPwd(pwd));
        memberEntity = memberRepository.save(memberEntity);

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setUid(memberEntity.getUid());
        accountEntity.setType(AccountEntity.ACCOUNT_TYPE.TELEPHONE);
        accountEntity.setAccount(tel);

        // FIXME: 2017/9/4 这里要处理唯一索引问题
        accountRepository.save(accountEntity);

        return ServiceDomainEmptyMessage.newMessage();
    }

    @Transactional(readOnly = true)
    @Override
    public ServiceDomainMessage<MemberEntity> singInWithTel(@NotNull String tel, @NotNull String pwd) {
        AccountEntity entity = accountRepository.findByAccountAndType(tel, AccountEntity.ACCOUNT_TYPE.TELEPHONE);
        if (entity == null) {
            return ServiceDomainMessage.newMessage(ERROR_CODE_CONSTANTS.ACCOUNT_NOT_FOUND);
        }

        MemberEntity memberEntity = memberRepository.findOne(entity.getUid());
        if (memberEntity == null) {
            return ServiceDomainMessage.newMessage(ERROR_CODE_CONSTANTS.ACCOUNT_MEMBER_NOT_FOUND);
        }

        String inputHashPwd = PWDUtils.createPwd(pwd);
        if (!inputHashPwd.equals(memberEntity.getPwd())) {
            return ServiceDomainMessage.newMessage(ERROR_CODE_CONSTANTS.PASSWORD_VALID_ERROR);
        }

        return ServiceDomainMessage.newMessage(memberEntity);
    }

}
