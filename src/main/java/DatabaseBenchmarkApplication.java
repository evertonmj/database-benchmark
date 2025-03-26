import benchmarks.keyvalue.tests.DynamoDBOperations;
import benchmarks.keyvalue.tests.RedisOperations;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.EndOfFileException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import benchmarks.nosql_sql.RunSQLNoSQLTests;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static benchmarks.keyvalue.tests.DynamoDBOperations.createTableIfNotExists;

public class DatabaseBenchmarkApplication {

    public static void main(String[] args) {

        int testSize = 1000;
        System.out.println("Starting KeyValue Tests");
        System.out.println("Redis - Writing "+testSize+" key-value pairs");
        long writeTimeRedis = RedisOperations.write(testSize);
        System.out.println("Redis - Reading "+testSize+" key-value pairs");
        long readTimeRedis = RedisOperations.read(testSize);

        System.out.println("REDIS - KeyValue Tests");
        System.out.println("Write Time: " + writeTimeRedis + " ms");
        System.out.println("Read Time: " + readTimeRedis + " ms");

        System.out.println("Starting DynamoDB Tests");
        System.out.println("DynamoDB - Writing "+testSize+" key-value pairs");

        AmazonDynamoDB dynamoDbClient = DynamoDBOperations.config();
        createTableIfNotExists(dynamoDbClient);

        long writeTimeDynamoDB = DynamoDBOperations.write(dynamoDbClient, testSize);
        System.out.println("DynamoDB - Reading "+testSize+" key-value pairs");
        long readTimeDynamoDB = DynamoDBOperations.read(dynamoDbClient, testSize);

        System.out.println("DynamoDB - KeyValue Tests");
        System.out.println("Write Time: " + writeTimeDynamoDB + " ms");
        System.out.println("Read Time: " + readTimeDynamoDB + " ms");
//         System.out.println(RedisOperations.read(10000));
//        try {
//            Terminal terminal = TerminalBuilder.builder().system(true).build();
//            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
//
//            String prompt = "shell> ";
//            String line;
//
//            while (true) {
//                try {
//                    line = reader.readLine(prompt);
//                    if (line.equalsIgnoreCase("exit")) {
//                        break;
//                    }
//                    handleCommand(line);
//                } catch (UserInterruptException e) {
//                    // Handle Ctrl+C
//                    break;
//                } catch (EndOfFileException e) {
//                    // Handle Ctrl+D
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private static void handleCommand(String command) {
        switch (command) {
            case "run all tests":
                RunSQLNoSQLTests.runAllTests();
                break;
            case "run full search test":
                RunSQLNoSQLTests.runFullTextSearchTests();
                break;
            case "run sequential read/write test":
                RunSQLNoSQLTests.runSequentialReadWriteTests();
                break;
            default:
                System.out.println("Unknown command: " + command);
        }
    }
}