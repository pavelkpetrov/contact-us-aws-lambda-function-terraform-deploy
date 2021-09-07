package com.aws.lambda.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

/**
 * Just an util class for an eager initialization of sdk clients.
 */
public class AwsClientFactory {

    private final DynamoDB dynamoDB;

    /**
     * AWS regions should be env variables if you want to generalize the solution.
     */
    public AwsClientFactory() {
        AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_WEST_1).build();
        dynamoDB = new DynamoDB(dynamoDBClient);
    }

    public DynamoDB getDynamoDB() {
        return dynamoDB;
    }

}