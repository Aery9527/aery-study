package org.aery.study.spring.redis;

import org.aery.study.spring.redis._test.RedisEmbeddedServerConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@SpringBootTest(classes = RedisEmbeddedServerConfig.class)
@ActiveProfiles("test")
@DirtiesContext
public class ZSetOperationsTest {

    @Autowired
    private ZSetOperations<String, Object> redisZsetOps;

    @Test
    public void test_add_range_del() {
        final String key = UUID.randomUUID().toString();
        final boolean insertValue = true;
        final boolean updateValue = false;

        Boolean addResult = this.redisZsetOps.add(key, "a", 10);
        Assertions.assertThat(addResult).isEqualTo(insertValue);

        addResult = this.redisZsetOps.add(key, "b", 20);
        Assertions.assertThat(addResult).isEqualTo(insertValue);

        Set<Object> member = this.redisZsetOps.range(key, 0, 0); // 取出排名0~0的元素
        Assertions.assertThat(member).containsExactly("a");

        addResult = this.redisZsetOps.add(key, "b", 5);
        Assertions.assertThat(addResult).isEqualTo(updateValue); // 因為b已經存在所以會回傳false

        addResult = this.redisZsetOps.add(key, "c", 1);
        Assertions.assertThat(addResult).isEqualTo(insertValue);

        member = this.redisZsetOps.range(key, 0, -1); // 取出全部的意思
        Assertions.assertThat(member).containsExactly("c", "b", "a"); // 比對元素順序
    }

    @Test
    public void test_size_count_del() {
        final String key = UUID.randomUUID().toString();

        Long size = this.redisZsetOps.size(key);
        Assertions.assertThat(size).isEqualTo(0);

        Long count = this.redisZsetOps.count(key, 10, 20);
        Assertions.assertThat(count).isEqualTo(0);

        this.redisZsetOps.add(key, "a", 15);
        this.redisZsetOps.add(key, "b", 21);
        this.redisZsetOps.add(key, "c", 9);
        this.redisZsetOps.add(key, "d", 18);

        size = this.redisZsetOps.size(key);
        Assertions.assertThat(size).isEqualTo(4);

        count = this.redisZsetOps.count(key, 10, 20);
        Assertions.assertThat(count).isEqualTo(2);

        count = this.redisZsetOps.count(key, 9, 20); // 下限包含該scope
        Assertions.assertThat(count).isEqualTo(3);

        count = this.redisZsetOps.count(key, 9, 21); // 上限包含該scope
        Assertions.assertThat(count).isEqualTo(4);
    }

    /**
     * 用來測試當score當相同時, 每次查詢其排序是否都相同
     */
    @Test
    public void test_score_same_order_del() {
        for (int randomTimes = 1; randomTimes <= 10; randomTimes++) {
            final String key = UUID.randomUUID().toString();
            final double score = 100;

            for (int number = 1; number <= 10; number++) {
                String val = String.valueOf((Math.random() * 100));
                this.redisZsetOps.add(key, val, score); // 若一開始連線不能用會爆出RedisConnectionFailureException, 若斷線報出QueryTimeoutException
            }

            Set<Object> base = this.redisZsetOps.range(key, 0, -1);

            int testTimes = 100;
            for (int i = 1; i <= testTimes; i++) {
                Set<Object> members = this.redisZsetOps.range(key, 0, -1);
                Assertions.assertThat(members).containsExactlyElementsOf(base);
            }
        }
    }

    @Test
    public void test_rank() {
        final String key = UUID.randomUUID().toString();
        this.redisZsetOps.add(key, "a", 100);
        this.redisZsetOps.add(key, "b", 10);

        Assertions.assertThat(this.redisZsetOps.rank(key, "a")).isEqualTo(1);
        Assertions.assertThat(this.redisZsetOps.rank(key, "b")).isEqualTo(0);
        Assertions.assertThat(this.redisZsetOps.rank(key, "c")).isEqualTo(null);
    }

    @Test
    public void test_scan() throws IOException {
        final String key = UUID.randomUUID().toString();
        this.redisZsetOps.add(key, "a", 100);
        this.redisZsetOps.add(key, "b", 10);
        this.redisZsetOps.add(key, "c", 101);

        int count = 1;

        ScanOptions.ScanOptionsBuilder builder = ScanOptions.scanOptions();
        builder.match("*");
        ScanOptions scanOptions = builder.build();

        Cursor<ZSetOperations.TypedTuple<Object>> cursor = this.redisZsetOps.scan(key, scanOptions);
        while (cursor.hasNext()) {
            ZSetOperations.TypedTuple<Object> tuple = cursor.next();
            Object value = tuple.getValue();
            Double score = tuple.getScore();

            if (count == 1) {
                Assertions.assertThat(value).isEqualTo("b");
                Assertions.assertThat(score).isEqualTo(10);
            } else if (count == 2) {
                Assertions.assertThat(value).isEqualTo("a");
                Assertions.assertThat(score).isEqualTo(100);
            } else if (count == 3) {
                Assertions.assertThat(value).isEqualTo("c");
                Assertions.assertThat(score).isEqualTo(101);
            }

            count++;
        }

        cursor.close();
    }

    @Test
    public void test() {

    }

}

