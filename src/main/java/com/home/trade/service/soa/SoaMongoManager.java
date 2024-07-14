package com.home.trade.service.soa;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class SoaMongoManager {

    private final MongoTemplate mongoTemplate;

    public SoaMongoManager(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public boolean isCollectionExists(String collectionName) {
        return mongoTemplate.collectionExists(collectionName);
    }

    public void createCollection(String collectionName) {
        mongoTemplate.createCollection(collectionName);
    }

    public void insert(Collection<?> batchToSave, String collectionName) {
        mongoTemplate.insert(batchToSave, collectionName);
    }

    public List<?> find(Query query, Class<?> entityClass, String collectionName) {
        return mongoTemplate.find(query, entityClass, collectionName);
    }

    public List<?> findAll(Class<?> entityClass, String collectionName) {
        return mongoTemplate.findAll(entityClass, collectionName);
    }
}
