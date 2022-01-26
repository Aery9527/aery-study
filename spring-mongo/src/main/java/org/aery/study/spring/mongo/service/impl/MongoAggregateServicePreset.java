package org.aery.study.spring.mongo.service.impl;

import org.aery.study.spring.mongo.service.api.MongoAggregateService;
import org.aery.study.spring.mongo.service.vo.AggregateResult1;
import org.aery.study.spring.mongo.service.vo.CollectionData1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Function;

@Service
public class MongoAggregateServicePreset implements MongoAggregateService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Map<String, List<CollectionData1>> initialData(List<CollectionData1> initDatas, String collectionPrefix) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Function<Long, String> fetchDateFromTimestamp = (ts) -> sdf.format(new Date(ts));

        Map<String, List<CollectionData1>> result = initDatas.stream()
                .map(data -> new AbstractMap.SimpleEntry<>(fetchDateFromTimestamp.apply(data.getStartTime()), data))
                .reduce(new HashMap<>(), (map, entry) -> {
                    String key = entry.getKey();
                    CollectionData1 val = entry.getValue();
                    map.computeIfAbsent(key, (k) -> new ArrayList<>()).add(val);
                    return map;
                }, (m1, m2) -> null);

        result.forEach((date, dataOfList) -> {
            String collectionName = collectionPrefix + date;
            this.mongoTemplate.insert(dataOfList, collectionName);
        });

        return result;
    }

    @Override
    public AggregateResult1 aggregate1(String collectionPrefix, String brand, String siteId, LocalDateTime offsetTime, int searchHours) {
        LocalDateTime startLDT = searchHours >= 0 ? offsetTime : offsetTime.plusHours(searchHours);
        LocalDateTime endLDT = searchHours >= 0 ? offsetTime.plusHours(searchHours) : offsetTime;

        String collectionName = collectionPrefix + YearMonth.from(startLDT).toString().replace("-", "");

        long startTs = Timestamp.valueOf(startLDT).getTime();
        long endTs = Timestamp.valueOf(endLDT).getTime() + 999;

        MatchOperation match = Aggregation.match(
                Criteria.where("brand").is(brand)
                        .and("siteId").is(siteId)
                        .and("startTime").gte(startTs).lte(endTs)
        );

        ProjectionOperation project = Aggregation.project()
                .andExpression("brand").as("brand")
                .andExpression("siteId").as("siteId")
                .andExpression("bulletCount").as("count")
                .andExpression("putIntoAmt").as("input")
                .andExpression("outPutAmt").as("output");

        GroupOperation group = Aggregation.group("brand", "siteId")
                .sum("count").as("count")
                .sum("input").as("input")
                .sum("output").as("output");

        Aggregation aggregation = Aggregation.newAggregation(match, project, group);

        AggregationResults<AggregateResult1> aggResult = this.mongoTemplate.aggregate(aggregation, collectionName, AggregateResult1.class);
        List<AggregateResult1> resultList = aggResult.getMappedResults();

        if (resultList.isEmpty()) {
            Map<String, String> id = new HashMap<>();
            id.put("brand", brand);
            id.put("siteId", siteId);

            AggregateResult1 result = new AggregateResult1();
            result.setId(id);
            result.setCount(BigDecimal.ZERO);
            result.setInput(BigDecimal.ZERO);
            result.setOutput(BigDecimal.ZERO);

            return result;
        }

        AggregateResult1 result = resultList.get(0);
        BigDecimal input = result.getInput();
        BigDecimal output = result.getOutput();
        BigDecimal oneHundred = BigDecimal.valueOf(100);
        result.setInput(input.divide(oneHundred, 2, RoundingMode.HALF_UP));
        result.setOutput(output.divide(oneHundred, 2, RoundingMode.HALF_UP));

        return result;
    }

}
