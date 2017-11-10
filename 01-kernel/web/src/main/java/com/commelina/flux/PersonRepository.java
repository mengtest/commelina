package com.commelina.flux;

import org.springframework.data.repository.CrudRepository;

/**
 * @author panyao
 * @date 2017/11/9
 */
public interface PersonRepository extends CrudRepository<User, Long> {
}
