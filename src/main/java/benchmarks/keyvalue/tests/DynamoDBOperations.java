package benchmarks.keyvalue.tests;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.HashMap;

public class DynamoDBOperations {

    public static AmazonDynamoDB config() {
        AmazonDynamoDB dynamoDbClient;

        String accessKey = "dummy";
        String secretKey = "dummy";
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(accessKey, secretKey));

        dynamoDbClient = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
                .build();

        return dynamoDbClient;
    }

    public static long read(AmazonDynamoDB dynamoDbClient, int iteration_size) {
        long startTime = System.nanoTime();
        try {
            for (int i = 0; i < iteration_size; i++) {
                HashMap<String, AttributeValue> key = new HashMap<>();
                key.put("Name", new AttributeValue("TestData" + i)); // match keys inserted

                GetItemRequest request = new GetItemRequest()
                        .withTableName("test_table")
                        .withKey(key);
                dynamoDbClient.getItem(request);
            }
        } catch (ResourceNotFoundException e) {
            System.err.format("Error: The table \"%s\" can't be found.%n", "test_table");
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    public static long write(AmazonDynamoDB dynamoDbClient, int iteration_size) {
        System.out.println("Starting write...");
        long startTime = System.nanoTime();
        try {
            for (int i = 0; i < iteration_size; i++) {
                HashMap<String, AttributeValue> item_values = new HashMap<>();
                item_values.put("Name", new AttributeValue("TestData" + i));

                PutItemRequest request = new PutItemRequest()
                        .withTableName("test_table")
                        .withItem(item_values);

                dynamoDbClient.putItem(request);
            }
        } catch (ResourceNotFoundException e) {
            System.err.format("Error: The table \"%s\" can't be found.%n", "test_table");
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    public static void createTableIfNotExists(AmazonDynamoDB dynamoDbClient) {
        try {
            dynamoDbClient.describeTable("test_table");
            System.out.println("Table already exists.");
        } catch (ResourceNotFoundException e) {
            CreateTableRequest request = new CreateTableRequest()
                    .withAttributeDefinitions(new AttributeDefinition("Name", ScalarAttributeType.S))
                    .withKeySchema(new KeySchemaElement("Name", KeyType.HASH))
                    .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L))
                    .withTableName("test_table");

            CreateTableResult result = dynamoDbClient.createTable(request);
            System.out.println("Table created: " + result.getTableDescription().getTableName());

            waitForTableToBecomeActive(dynamoDbClient, "test_table");
        } catch (AmazonServiceException ex) {
            System.err.println(ex.getErrorMessage());
        }
    }

    private static void waitForTableToBecomeActive(AmazonDynamoDB dynamoDbClient, String tableName) {
        System.out.print("Waiting for table to become ACTIVE...");
        boolean isActive = false;
        while (!isActive) {
            try {
                Thread.sleep(1000);
                TableDescription table = dynamoDbClient.describeTable(tableName).getTable();
                if (table.getTableStatus().equals("ACTIVE")) {
                    isActive = true;
                }
                System.out.print(".");
            } catch (InterruptedException e) {
                System.err.println("Interrupted while waiting for table activation.");
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("\nTable is ACTIVE.");
    }
}
