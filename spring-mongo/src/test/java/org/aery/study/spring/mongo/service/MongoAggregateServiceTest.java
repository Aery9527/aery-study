package org.aery.study.spring.mongo.service;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.aery.study.spring.mongo.SpringMongoStudyApplication;
import org.aery.study.spring.mongo.service.api.MongoAggregateService;
import org.aery.study.spring.mongo.service.vo.CollectionData1;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

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
        List<CollectionData1> initDatas = Collections.emptyList();

        this.mongoAggregateService.initialData(initDatas);


    }

}
