package org.aery.study.spring.mongo.service.api;

import org.aery.study.spring.mongo.service.vo.AggregateResult1;
import org.aery.study.spring.mongo.service.vo.CollectionData1;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MongoAggregateService {

    void initialData(List<CollectionData1> initDatas);

    List<AggregateResult1> aggregate1();

}
