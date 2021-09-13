package org.aery.study.spring.redis.entity.repository;

import org.aery.study.spring.redis.entity.Man;
import org.springframework.data.repository.CrudRepository;

public interface ManRepository extends CrudRepository<Man, String> {

}
