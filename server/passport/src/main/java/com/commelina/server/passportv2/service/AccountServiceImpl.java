package com.commelina.server.passportv2.service;

import com.commelina.server.passport.proto.ERROR_CODE;
import com.commelina.server.passportv2.dao.AccountTelephoneRepository;
import com.commelina.server.passportv2.dao.MemberRepository;
import com.commelina.server.passportv2.entity.AccountTelephoneEntity;
import com.commelina.server.passportv2.entity.MemberEntity;
import com.commelina.utils.ServiceDomainEmptyMessage;
import com.commelina.utils.ServiceDomainMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author panyao
 * @date 2017/9/2
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountTelephoneRepository accountTephoneRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public ServiceDomainMessage<MemberEntity> singInWithTelOrNoPassword(String tel) {
        AccountTelephoneEntity entity = accountTephoneRepository.findByAccount(tel);
        if (entity != null) {
            return ServiceDomainMessage.newMessage(memberRepository.findById(entity.getUid()).get());
        }

        if (entity != null) {
            MemberEntity memberEntity = memberRepository.findById(entity.getUid()).orElse(null);

            if (memberEntity == null) {
                return ServiceDomainMessage.newMessage(ERROR_CODE.ACCOUNT_MEMBER_NOT_FOUND);
            }

            return ServiceDomainMessage.newMessage(memberEntity);
        }

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setPwd(PwdUtils.createPwd(tel));
        memberEntity = memberRepository.save(memberEntity);

        AccountTelephoneEntity accountEntity = new AccountTelephoneEntity();
        accountEntity.setUid(memberEntity.getUid());
        accountEntity.setAccount(tel);

        // FIXME: 2017/9/4 这里要处理唯一索引问题
        accountTephoneRepository.save(accountEntity);

        return ServiceDomainMessage.newMessage(memberEntity);
    }

    /**
     * @param tel
     * @param pwd
     * @return
     */
    @Transactional
    @Override
    public ServiceDomainEmptyMessage registerTel(String tel, String pwd) {
        AccountTelephoneEntity entity = accountTephoneRepository.findByAccount(tel);
        if (entity != null) {
            return ServiceDomainEmptyMessage.newMessage(ERROR_CODE.ACCOUNT_EXISTS);
        }
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setPwd(PwdUtils.createPwd(pwd));
        memberEntity = memberRepository.save(memberEntity);

        AccountTelephoneEntity accountEntity = new AccountTelephoneEntity();
        accountEntity.setUid(memberEntity.getUid());
        accountEntity.setAccount(tel);

        // FIXME: 2017/9/4 这里要处理唯一索引问题
        accountTephoneRepository.save(accountEntity);

        return ServiceDomainEmptyMessage.newMessage();
    }

    @Transactional(readOnly = true)
    @Override
    public ServiceDomainMessage<MemberEntity> signInWithTel(String tel, String pwd) {
        AccountTelephoneEntity entity = accountTephoneRepository.findByAccount(tel);
        if (entity == null) {
            return ServiceDomainMessage.newMessage(ERROR_CODE.ACCOUNT_NOT_FOUND);
        }

        MemberEntity memberEntity = memberRepository.findById(entity.getUid()).orElse(null);
        if (memberEntity == null) {
            return ServiceDomainMessage.newMessage(ERROR_CODE.ACCOUNT_MEMBER_NOT_FOUND);
        }

        String inputHashPwd = PwdUtils.createPwd(pwd);
        if (!inputHashPwd.equals(memberEntity.getPwd())) {
            return ServiceDomainMessage.newMessage(ERROR_CODE.PASSWORD_VALID_ERROR);
        }

        return ServiceDomainMessage.newMessage(memberEntity);
    }

}
