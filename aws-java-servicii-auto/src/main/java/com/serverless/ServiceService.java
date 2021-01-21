package com.serverless;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.serverless.dao.DynamoDBAdapter;
import com.serverless.dao.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ServiceService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    DynamoDBAdapter db_adapter;
    AmazonDynamoDB client;
    DynamoDBMapper mapper;
    private static final String SERVICES_TABLE_NAME = System.getenv("SERVICES_TABLE_NAME");

    public ServiceService() {
        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(SERVICES_TABLE_NAME))
                .build();
        this.db_adapter = DynamoDBAdapter.getInstance();
        this.client = this.db_adapter.getClient();
        this.mapper = this.db_adapter.createDbMapper(mapperConfig);
    }

    public void save(Service service) {
        logger.info("Services - save(): " + service.toString());
        this.mapper.save(service);
    }

    public List<Service> list() {
        DynamoDBScanExpression dbScanExpression = new DynamoDBScanExpression();
        List<Service> results = this.mapper.scan(Service.class, dbScanExpression);
        for (Service service : results) {
            logger.info("Services - list(): " + service.toString());
        }
        return results;
    }

    public Service get(String id) {
        Service service = null;
        HashMap<String, AttributeValue> attributeValue = new HashMap<String, AttributeValue>();
        attributeValue.put(":v1", new AttributeValue().withS(id));
        DynamoDBQueryExpression<Service> queryExp = new DynamoDBQueryExpression<Service>()
                .withKeyConditionExpression("id = :v1")
                .withExpressionAttributeValues(attributeValue);
        PaginatedQueryList<Service> result = this.mapper.query(Service.class, queryExp);
        if (result.size() > 0) {
            service = result.get(0);
            logger.info("Services - get(): service - " + service.toString());
        } else {
            logger.info("Services - get(): service - Not Found.");
        }
        return service;
    }

    public Boolean delete(String id) {
        Service service;
        service = get(id);
        if (service != null) {
            logger.info("Services - delete(): " + service.toString());
            this.mapper.delete(service);
        } else {
            logger.info("Services - delete(): service - does not exist.");
            return false;
        }
        return true;
    }
}
