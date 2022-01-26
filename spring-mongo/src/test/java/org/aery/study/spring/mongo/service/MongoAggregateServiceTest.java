package org.aery.study.spring.mongo.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.aery.study.spring.mongo.SpringMongoStudyApplication;
import org.aery.study.spring.mongo.service.api.MongoAggregateService;
import org.aery.study.spring.mongo.service.vo.AggregateResult1;
import org.aery.study.spring.mongo.service.vo.CollectionData1;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackageClasses = SpringMongoStudyApplication.class)
class MongoAggregateServiceTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoAggregateService mongoAggregateService;

    @DisplayName("test")
    @Test
    public void test() {
        final String collectionName = "ccc";

        DBObject objectToSave1 = BasicDBObjectBuilder.start()
                .add("key", "value")
                .get();
        this.logger.info(objectToSave1.toString());

        DBObject objectToSave2 = this.mongoTemplate.save(objectToSave1, collectionName);
        this.logger.info(objectToSave2.toString());

        List<DBObject> finds = this.mongoTemplate.findAll(DBObject.class, collectionName);
        this.logger.info(finds.toString());

        Assertions.assertThat(finds)
                .extracting("key")
                .containsOnly("value");
    }

    @DisplayName("aggregate1")
    @Test
    public void aggregate1() {
        String collectionPrefix = "kerker_";

        List<String> brands = Arrays.asList("aery", "rion");
        List<String> sites = Arrays.asList("9527", "5566", "0204");
        AtomicInteger brandsIndex = new AtomicInteger(0);
        AtomicInteger sitesIndex = new AtomicInteger(0);
        BiFunction<List<String>, AtomicInteger, String> elementFetcher = (list, indexCounter) -> {
            int index = indexCounter.getAndIncrement();
            if (index >= list.size()) {
                indexCounter.set(1);
                index = 0;
            }
            return list.get(index);
        };
        IntUnaryOperator randomSupplier = (limit) -> (int) (Math.random() * (limit - 1)) + 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Function<String, CollectionData1> dataGenerator = (date) -> {
            CollectionData1 data = new CollectionData1();
            data.setBrand(elementFetcher.apply(brands, brandsIndex));
            data.setSiteId(elementFetcher.apply(sites, sitesIndex));
            data.setBulletCount(randomSupplier.applyAsInt(10));
            data.setPutIntoAmt(randomSupplier.applyAsInt(100));
            data.setOutPutAmt(randomSupplier.applyAsInt(100));
            try {
                data.setStartTime(sdf.parse(date).getTime());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            data.setDate(date);

            return data;
        };

        List<CollectionData1> initDatas = Arrays.asList(
                dataGenerator.apply("2021-12-31 18:00:00"), dataGenerator.apply("2021-12-31 18:30:00"),
                dataGenerator.apply("2021-12-31 19:00:00"), dataGenerator.apply("2021-12-31 19:30:00"),
                dataGenerator.apply("2021-12-31 20:00:00"), dataGenerator.apply("2021-12-31 20:30:00"),
                dataGenerator.apply("2021-12-31 21:00:00"), dataGenerator.apply("2021-12-31 21:30:00"),
                dataGenerator.apply("2021-12-31 22:00:00"), dataGenerator.apply("2021-12-31 22:30:00"),
                dataGenerator.apply("2021-12-31 23:00:00"), dataGenerator.apply("2021-12-31 23:30:00"),
                dataGenerator.apply("2022-01-01 00:00:00"), dataGenerator.apply("2022-01-01 00:30:00"),
                dataGenerator.apply("2022-01-01 01:00:00"), dataGenerator.apply("2022-01-01 01:30:00"),
                dataGenerator.apply("2022-01-01 02:00:00"), dataGenerator.apply("2022-01-01 02:30:00"),
                dataGenerator.apply("2022-01-01 03:00:00"), dataGenerator.apply("2022-01-01 03:30:00"),
                dataGenerator.apply("2022-01-01 04:00:00"), dataGenerator.apply("2022-01-01 04:30:00"),
                dataGenerator.apply("2022-01-01 05:00:00"), dataGenerator.apply("2022-01-01 05:30:00")
        );

        Map<String, List<CollectionData1>> initResult = this.mongoAggregateService.initialData(initDatas, collectionPrefix);
        this.logger.info("initResult: " + JSON.toJSONString(initResult));

        // 條件
        String brand = brands.get(0);
        String siteId = sites.get(0);
        LocalDateTime offsetTime = LocalDateTime.parse("2022-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int searchHours = 4;

        AggregateResult1 aggrResult = this.mongoAggregateService.aggregate1(collectionPrefix, brand, siteId, offsetTime, searchHours);
        Map<String, String> id = aggrResult.getId();
        BigDecimal aggrCount = aggrResult.getCount();
        BigDecimal aggrInput = aggrResult.getInput();
        BigDecimal aggrOutput = aggrResult.getOutput();

        final String fieldCount = "count";
        final String fieldInput = "input";
        final String fieldOutput = "output";
        LocalDateTime startLDT = searchHours >= 0 ? offsetTime : offsetTime.plusHours(searchHours);
        LocalDateTime endLDT = searchHours >= 0 ? offsetTime.plusHours(searchHours) : offsetTime;
        long startTs = Timestamp.valueOf(startLDT).getTime();
        long endTs = Timestamp.valueOf(endLDT).getTime() + 999;

        BigDecimal oneHundred = BigDecimal.valueOf(100);
        IntFunction<BigDecimal> mapToBigDecimal = (number) -> new BigDecimal(number).divide(oneHundred, 2, RoundingMode.HALF_UP);
        Map<String, BigDecimal> testResult = initDatas.stream()
                .filter((data) -> data.getBrand().equals(brand) && data.getSiteId().equals(siteId))
                .filter((data) -> {
                    Long startTime = data.getStartTime();
                    return startTime >= startTs && startTime <= endTs;
                })
                .map((data) -> {
                    Map<String, BigDecimal> map = new HashMap<>();
                    map.put(fieldCount, new BigDecimal(data.getBulletCount()));
                    map.put(fieldInput, mapToBigDecimal.apply(data.getPutIntoAmt()));
                    map.put(fieldOutput, mapToBigDecimal.apply(data.getOutPutAmt()));
                    return map;
                })
                .reduce((map1, map2) -> {
                    map1.put(fieldCount, map1.get(fieldCount).add(map2.get(fieldCount)));
                    map1.put(fieldInput, map1.get(fieldInput).add(map2.get(fieldInput)));
                    map1.put(fieldOutput, map1.get(fieldOutput).add(map2.get(fieldOutput)));
                    return map1;
                }).get();

        Assertions.assertThat(id).containsExactly(
                new AbstractMap.SimpleEntry<>("brand", brand),
                new AbstractMap.SimpleEntry<>("siteId", siteId)
        );
        Assertions.assertThat(aggrCount).isEqualTo(testResult.get(fieldCount));
        Assertions.assertThat(aggrInput).isEqualTo(testResult.get(fieldInput));
        Assertions.assertThat(aggrOutput).isEqualTo(testResult.get(fieldOutput));
    }

}
