package benchmarks.keyvalue;

import benchmarks.keyvalue.tests.DynamoDBOperations;
import benchmarks.keyvalue.tests.RedisOperations;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

public class RunDBKeyValueTests {

    public void runKeyValueTests() {
        long writeTimeRedis = RedisOperations.write(100000);
        long readTimeRedis = RedisOperations    .read(100000);

        System.out.println("REDIS - KeyValue Tests");
        System.out.println("Write Time: " + writeTimeRedis + " ms");
        System.out.println("Read Time: " + readTimeRedis + " ms");

        AmazonDynamoDB dynamoDbClient = DynamoDBOperations.config();
        long writeTimeDynamoDB = DynamoDBOperations.write(dynamoDbClient, 100000);
        long readTimeDynamoDB = DynamoDBOperations.read(dynamoDbClient, 100000);

        System.out.println("DynamoDB - KeyValue Tests");
        System.out.println("Write Time: " + writeTimeDynamoDB + " ms");
        System.out.println("Read Time: " + readTimeDynamoDB + " ms");
    }
}
