package org.aery.study.spring.mongo.service.impl;

import org.aery.study.spring.mongo.SpringMongoStudyApplication;
import org.aery.study.spring.mongo.service.api.MongoCollectionsService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@DataMongoTest(properties = "spring.mongodb.embedded.version=3.5.5")
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackageClasses = SpringMongoStudyApplication.class)
class MongoCollectionsServicePresetTest {

    public static class TestData {
        private Integer a;

        public TestData() {
        }

        public TestData(Integer a) {
            this.a = a;
        }

        public Integer getA() {
            return a;
        }

        public void setA(Integer a) {
            this.a = a;
        }
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoCollectionsService mongoCollectionsService;

    @Test
    void createCappedCollection() {
        String collectionName = "kerker";
        boolean created = this.mongoCollectionsService.createCappedCollection(collectionName, 2);
        Assertions.assertThat(created).isTrue();

        this.mongoTemplate.insert(new TestData(1), collectionName);
        this.mongoTemplate.insert(new TestData(2), collectionName);

        List<TestData> checkResult1 = this.mongoTemplate.findAll(TestData.class, collectionName);
        Assertions.assertThat(checkResult1).hasSize(2);
        Assertions.assertThat(checkResult1.get(0).getA()).isEqualTo(1);
        Assertions.assertThat(checkResult1.get(1).getA()).isEqualTo(2);

        this.mongoTemplate.insert(new TestData(3), collectionName);
        List<TestData> checkResult2 = this.mongoTemplate.findAll(TestData.class, collectionName);
        Assertions.assertThat(checkResult2).hasSize(2);
        Assertions.assertThat(checkResult2.get(0).getA()).isEqualTo(2);
        Assertions.assertThat(checkResult2.get(1).getA()).isEqualTo(3);

        created = this.mongoCollectionsService.createCappedCollection(collectionName, 3);
        Assertions.assertThat(created).isFalse();
    }

}
