package com.app.passport.dao;

import com.app.passport.entity.AccountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by panyao on 2017/9/2.
 */
@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Long> {

    AccountEntity findByAccountAndType(String account, AccountEntity.ACCOUNT_TYPE type);

}
