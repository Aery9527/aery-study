package org.aery.study.spring.mongo.service.impl;

import org.aery.study.spring.mongo.service.api.MongoCollectionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class MongoCollectionsServicePreset implements MongoCollectionsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public boolean createCappedCollection(String collectionName, long maxDocuments, long eachSize) {
        try {
            long cappedSize = maxDocuments * eachSize;
            CollectionOptions options = new CollectionOptions(cappedSize, maxDocuments, true);
            this.mongoTemplate.createCollection(collectionName, options);
            return true;
        } catch (UncategorizedMongoDbException e) {
            return false;
        } catch (Exception e) {
            this.logger.error("", e);
            return false;
        }
    }

}
