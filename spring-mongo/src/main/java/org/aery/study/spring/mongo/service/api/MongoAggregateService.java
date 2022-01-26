package org.aery.study.spring.mongo.service.api;

import org.aery.study.spring.mongo.service.vo.AggregateResult1;
import org.aery.study.spring.mongo.service.vo.CollectionData1;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public interface MongoAggregateService {

    Map<String, List<CollectionData1>> initialData(List<CollectionData1> initDatas, String collectionPrefix);

    AggregateResult1 aggregate1(String collectionPrefix, String brand, String siteId, LocalDateTime offsetTime, int searchHours);

}
