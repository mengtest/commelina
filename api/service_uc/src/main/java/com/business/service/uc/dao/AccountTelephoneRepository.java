package com.business.service.uc.dao;

import com.business.service.uc.entity.AccountTelephoneEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author panyao
 * @date 2017/9/2
 */
@Repository
public interface AccountTelephoneRepository extends CrudRepository<AccountTelephoneEntity, Long> {

    /**
     * 根据账号和类型查询用户帐户
     *
     * @param account
     * @return
     */
    AccountTelephoneEntity findByAccountAndType(String account);

}
