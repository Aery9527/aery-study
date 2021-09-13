package org.aery.study.spring.redis.entity.repository;

import org.aery.study.spring.redis.entity.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, String> {

}
