package com.github.freedompy.commelina.server.passport.dao;

import com.business.service.passport.entity.AccountTelephoneEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author panyao
 * @date 2017/9/2
 */
@Repository("accountTelephoneRepository")
public interface AccountTelephoneRepository extends CrudRepository<AccountTelephoneEntity, Long> {

    /**
     * 根据账号和类型查询用户帐户
     *
     * @param account
     * @return
     */
    AccountTelephoneEntity findByAccountAndType(String account);

}
