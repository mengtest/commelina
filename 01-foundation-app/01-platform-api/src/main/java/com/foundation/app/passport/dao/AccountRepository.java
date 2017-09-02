package com.foundation.app.passport.dao;

import com.foundation.app.passport.entity.AccountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by panyao on 2017/9/2.
 */
@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, Long> {

    AccountEntity findByAcountAndType(String account, AccountEntity.ACCOUNT_TYPE type);

}
