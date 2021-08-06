package org.aery.study.spring.redis.service.api;

import org.aery.study.spring.redis.service.vo.RedisExploredResult;

public interface RedisDataExplorer {

    default RedisExploredResult explore() {
        return explore("*", false);
    }

    default RedisExploredResult explore(boolean fetchAllData) {
        return explore("*", fetchAllData);
    }

    default RedisExploredResult explore(String pattern) {
        return explore(pattern, false);
    }

    RedisExploredResult explore(String pattern, boolean fetchAllData);

}
