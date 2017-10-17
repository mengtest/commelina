package com.app.passport.dao;

import com.app.passport.entity.AccountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author panyao
 * @date 2017/9/2
 */
@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Long> {

    /**
     * 根据账号和类型查询用户帐户
     *
     * @param account
     * @param type
     * @return
     */
    AccountEntity findByAccountAndType(String account, AccountEntity.ACCOUNT_TYPE type);

}
