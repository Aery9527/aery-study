package org.aery.study.spring.redis.entity.repository;

import org.aery.study.spring.redis.entity.DataEntity;
import org.springframework.data.repository.CrudRepository;

public interface DataRepository extends CrudRepository<DataEntity, String> {
}
