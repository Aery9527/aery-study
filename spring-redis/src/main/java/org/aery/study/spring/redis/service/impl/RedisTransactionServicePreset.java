package org.aery.study.spring.redis.service.impl;

import org.aery.study.spring.redis.service.api.RedisTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RedisTransactionServicePreset implements RedisTransactionService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void multiAndExec(String key, String initValue, String updatedValue, boolean thrownExceptionBeforeCommit) {
        ValueOperations<String, Object> ops = this.redisTemplate.opsForValue();
        ops.set(key, initValue);

        try {
            this.redisTemplate.multi();
            Object v = ops.get(key); // 一定開啟

            ops.set(key, updatedValue);

            if (thrownExceptionBeforeCommit) {
                throw new RuntimeException();
            }

            this.redisTemplate.exec();
        } catch (RuntimeException e) {
            try {
                this.redisTemplate.discard();
            } catch (Exception ex) {
                this.logger.error("", ex);
            }
            throw e;
        }
    }

    @Transactional
    @Override
    public void transaction(String key, String initValue, String updatedValue, boolean thrownExceptionBeforeCommit) {
        ValueOperations<String, Object> ops = this.redisTemplate.opsForValue();

        Object a = ops.get(key); // 喵的哩, spring官方doc說讀寫分離,  但測起來還是沒有啊...這邊還是回傳null

        ops.set(key, initValue);

        Object b = ops.get(key);

        ops.set(key, updatedValue);

        if (thrownExceptionBeforeCommit) {
            throw new RuntimeException();
        }
    }

}
