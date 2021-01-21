package com.serverless.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;


public class DynamoDBAdapter {

    private AmazonDynamoDB client;
    private DynamoDBMapper mapper;
    private static DynamoDBAdapter db_adapter;

    private DynamoDBAdapter() {
        this.client = AmazonDynamoDBClientBuilder.standard()
                .build();
    }

    public AmazonDynamoDB getClient() {
        return client;
    }

    public static DynamoDBAdapter getInstance() {
        if (db_adapter == null)
            db_adapter = new DynamoDBAdapter();
        return db_adapter;
    }

    public DynamoDBMapper createDbMapper(DynamoDBMapperConfig mapperConfig) {
        if (this.client != null)
            mapper = new DynamoDBMapper(this.client, mapperConfig);
        return this.mapper;
    }

}
