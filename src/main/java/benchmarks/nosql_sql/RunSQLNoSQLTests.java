package benchmarks.nosql_sql;

import benchmarks.data.DataGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static benchmarks.nosql_sql.tests.FullTextSearchTest.*;
import static benchmarks.nosql_sql.tests.SequentialExecutionTest.*;

public class RunSQLNoSQLTests {

    private static FileWriter fileWriter;

    static {
        try {
            fileWriter = new FileWriter("results.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void log(String content) {
        try {
            fileWriter.write(content + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runFullTextSearchTests() {
        HashMap<String, String> data = DataGenerator.generateData(10000);

        redisLoadData(data);
        postgresLoadData(data);

        List<String> searchTerms = DataGenerator.getSearchTermsFromData(data.values(), 5);
        performFullTextSearch("Brazil");
        performFullTextSearch("plastic");
        performFullTextSearch("United States");
        performFullTextSearch("Pindamanhagaba");
    }

    public static void runSequentialReadWriteTests() {
        log("--------- Starting Redis experiment ---------");

        log("** Executing reads");
        for (int i = 0; i < 3; i++) {
            log("1.000: " + stressTestRedisRead(1000) + " ms");
            log("10.000: " + stressTestRedisRead(10000) + " ms");
            log("100.000: " + stressTestRedisRead(100000) + " ms");
            log("1.000.000: " + stressTestRedisRead(1000000) + " ms");
        }

        log("** Executing writes");
        for (int i = 0; i < 3; i++) {
            log("1.000: " + stressTestRedisWrite(1000) + " ms");
            log("10.000: " + stressTestRedisWrite(10000) + " ms");
            log("100.000: " + stressTestRedisWrite(100000) + " ms");
            log("1.000.000: " + stressTestRedisWrite(1000000) + " ms");
        }

        log("--------- End Redis experiment ---------");

        log("--------- Starting Postgres experiment ---------");

        log("** Executing three reads");
        for (int i = 0; i < 3; i++) {
            log("1.000: " + stressTestPostgresRead(1000) + " ms");
            log("10.000: " + stressTestPostgresRead(10000) + " ms");
            log("100.000: " + stressTestPostgresRead(100000) + " ms");
            log("1.000.000: " + stressTestPostgresRead(1000000) + " ms");
        }

        log("** Executing three writes");
        for (int i = 0; i < 3; i++) {
            log("1.000: " + stressTestPostgresWrite(1000) + " ms");
            log("10.000: " + stressTestPostgresWrite(10000) + " ms");
            log("100.000: " + stressTestPostgresWrite(100000) + " ms");
            log("1.000.000: " + stressTestPostgresWrite(1000000) + " ms");
        }

        log("--------- End Postgres experiment ---------");
    }

    public static void runAllTests() {
        runFullTextSearchTests();
        runSequentialReadWriteTests();
    }
}