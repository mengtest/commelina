package com.business.service.uc.service;
import com.app.passport.proto.SERVICE_PASSPORT_ERROR_CODE;
import com.business.service.uc.dao.AccountTelephoneRepository;
import com.business.service.uc.dao.MemberRepository;
import com.business.service.uc.entity.AccountTelephoneEntity;
import com.business.service.uc.entity.MemberEntity;
import com.github.freedompy.commelina.utils.ServiceDomainEmptyMessage;
import com.github.freedompy.commelina.utils.ServiceDomainMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author panyao
 * @date 2017/9/2
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountTelephoneRepository accountTephoneRepository;

    @Resource
    private MemberRepository memberRepository;

    @Transactional
    @Override
    public ServiceDomainMessage<MemberEntity> singInWithTelAndNoPassword(String tel) {
        AccountTelephoneEntity entity = accountTephoneRepository.findByAccountAndType(tel);
        if (entity != null) {
            MemberEntity memberEntity = memberRepository.findOne(entity.getUid());
            if (memberEntity == null) {
                return ServiceDomainMessage.newMessage(SERVICE_PASSPORT_ERROR_CODE.ACCOUNT_MEMBER_NOT_FOUND);
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
        AccountTelephoneEntity entity = accountTephoneRepository.findByAccountAndType(tel);
        if (entity != null) {
            return ServiceDomainEmptyMessage.newMessage(SERVICE_PASSPORT_ERROR_CODE.ACCOUNT_EXISTS);
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
        AccountTelephoneEntity entity = accountTephoneRepository.findByAccountAndType(tel);
        if (entity == null) {
            return ServiceDomainMessage.newMessage(SERVICE_PASSPORT_ERROR_CODE.ACCOUNT_NOT_FOUND);
        }

        MemberEntity memberEntity = memberRepository.findOne(entity.getUid());
        if (memberEntity == null) {
            return ServiceDomainMessage.newMessage(SERVICE_PASSPORT_ERROR_CODE.ACCOUNT_MEMBER_NOT_FOUND);
        }

        String inputHashPwd = PwdUtils.createPwd(pwd);
        if (!inputHashPwd.equals(memberEntity.getPwd())) {
            return ServiceDomainMessage.newMessage(SERVICE_PASSPORT_ERROR_CODE.PASSWORD_VALID_ERROR);
        }

        return ServiceDomainMessage.newMessage(memberEntity);
    }

}
