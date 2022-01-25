package org.aery.study.spring.mongo.service.impl;

import org.aery.study.spring.mongo.service.api.MongoAggregateService;
import org.aery.study.spring.mongo.service.vo.AggregateResult1;
import org.aery.study.spring.mongo.service.vo.CollectionData1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoAggregateServicePreset implements MongoAggregateService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void initialData(List<CollectionData1> initDatas) {

    }

    @Override
    public List<AggregateResult1> aggregate1() {
        List<AggregateResult1> result = null;

        return result;
    }

}
