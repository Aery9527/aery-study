package org.aery.study.spring.mongo.service.api;

public interface MongoCollectionsService {

    default void createCappedCollection(String collectionName, long maxDocuments) {
        createCappedCollection(collectionName, maxDocuments, 1024);
    }

    void createCappedCollection(String collectionName, long maxDocuments, long eachSize);

}
