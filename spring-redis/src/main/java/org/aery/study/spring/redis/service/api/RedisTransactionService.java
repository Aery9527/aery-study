package org.aery.study.spring.redis.service.api;

public interface RedisTransactionService {

    void multiAndExec(String key, String initValue, String updatedValue, boolean thrownExceptionBeforeCommit);

    void transaction(String key, String initValue, String updatedValue, boolean thrownExceptionBeforeCommit);

}
