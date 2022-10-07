package org.aery.study.spring.mongo.service.api;

public interface MongoCollectionsService {

    default boolean createCappedCollection(String collectionName, long maxDocuments) {
        return createCappedCollection(collectionName, maxDocuments, 1024);
    }

    boolean createCappedCollection(String collectionName, long maxDocuments, long eachSize);

}
