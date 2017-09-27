package com.app.passport.dao;

import com.app.passport.entity.MemberEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by panyao on 2017/9/2.
 */
@Repository
public interface MemberRepository extends CrudRepository<MemberEntity, Long> {

}