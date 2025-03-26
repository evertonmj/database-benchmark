package benchmarks.nosql_sql.tests;

import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SequentialExecutionTest {
    private static final String REDIS_HOST = "127.0.0.1";
    private static final int REDIS_PORT = 6379;
    private static final long ITERATION_SIZE = 1000000;
    private static final String POSTGRES_URL = "jdbc:postgresql://127.0.0.1:5432/";
    private static final String POSTGRES_USER = "postgres";
    private static final String POSTGRES_PASSWORD = "Postgres2022!";

    public static long stressTestRedisWrite(int iteration_size) {
        long startTime = System.nanoTime();
        try (Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT)) {
            for (int i = 0; i < iteration_size; i++) {
                jedis.set("key" + i, "TestData" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        return  (endTime - startTime) / 1_000_000;
    }

    public static long stressTestRedisRead(int iteration_size) {
        long startTime = System.nanoTime();
        try (Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT)) {
            for (int i = 0; i < iteration_size; i++) {
                jedis.get("key" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        
        return  (endTime - startTime) / 1_000_000;
    }

public static long stressTestPostgresWrite(int iterations) {
        long startTime = System.nanoTime();
        try (Connection conn = DriverManager.getConnection(POSTGRES_URL, POSTGRES_USER, POSTGRES_PASSWORD)) {
            for (int i = 0; i < iterations; i++) {
                try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO test_table (data) VALUES (?)")) {
                    stmt.setString(1, "TestData" + i);
                    stmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        return  (endTime - startTime) / 1_000_000;
    }

    public static long stressTestPostgresRead(int iterations) {
        long startTime = System.nanoTime();
        try (Connection conn = DriverManager.getConnection(POSTGRES_URL, POSTGRES_USER, POSTGRES_PASSWORD)) {
            for (int i = 0; i < iterations; i++) {
                try (PreparedStatement stmt = conn.prepareStatement("SELECT data FROM test_table WHERE id = ?")) {
                    stmt.setInt(1, i);
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            rs.getString("data");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        return  (endTime - startTime) / 1_000_000;
    }


    private static void print(Object value) {
        System.out.println(value);
    }

    private static void printf(Object value) {
        System.out.print(value);
    }
}
