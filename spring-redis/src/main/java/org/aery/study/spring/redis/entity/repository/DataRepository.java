package org.aery.study.spring.redis.entity.repository;

import org.aery.study.spring.redis.entity.DataEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DataRepository extends CrudRepository<DataEntity, String> {

    List<DataEntity> findByIndex1(String index1);

    List<DataEntity> findByIndex2(String index2);

    List<DataEntity> findByIndex1AndIndex2(String index1, String index2);

    List<DataEntity> findByIndex1OrIndex2(String index1, String index2);

    Optional<DataEntity> findByParameter1(String parameter1);

    Optional<DataEntity> findByKerker(String index1);

}
