package org.aery.study.spring.redis.service.api;

import org.aery.study.spring.redis.service.vo.RedisExploredResult;

public interface RedisDataExplorer {

    default RedisExploredResult explore() {
        return explore("*", false);
    }

    default RedisExploredResult explore(String pattern) {
        return explore(pattern, false);
    }

    default RedisExploredResult explore(boolean fetchAllData) {
        return explore("*", fetchAllData);
    }

    RedisExploredResult explore(String pattern, boolean fetchAllData);

    default void printExplored() {
        printExplored("*", false);
    }

    default void printExplored(boolean fetchAllData) {
        printExplored("*", fetchAllData);
    }

    default void printExplored(String pattern) {
        printExplored(pattern, false);
    }

    void printExplored(String pattern, boolean fetchAllData);

}
