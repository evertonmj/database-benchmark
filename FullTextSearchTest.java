import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.json.Path2;
import redis.clients.jedis.search.*;
import redis.clients.jedis.exceptions.JedisDataException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.logging.Logger;

public class FullTextSearchTest {
    private static final String REDIS_HOST = "127.0.0.1";
    private static final int REDIS_PORT = 6379;
    private static final long ITERATION_SIZE = 1000000;
    private static final String POSTGRES_URL = "jdbc:postgresql://127.0.0.1:5432/";
    private static final String POSTGRES_USER = "postgres";
    private static final String POSTGRES_PASSWORD = "Postgres2022!";

    public static void main(String[] args) {
        HashMap<String, String> data = DataGenerator.generateData(1000);

        redisLoadData(data);
        postgresLoadData(data);

        performFullTextSearch("Brazil");
        performFullTextSearch("lalalala");
        performFullTextSearch("United States");
        performFullTextSearch("Pindamanhagaba");
    }

    private static void redisLoadData(HashMap<String, String> values) {
        long startTime = System.nanoTime();
        print("Loading data for Redis");
        try (UnifiedJedis jedis = new UnifiedJedis("http://" + REDIS_HOST + ":" + REDIS_PORT)) {
            try {
                jedis.ftDropIndexDD("data");
            } catch (JedisDataException e) {
                if (e.getMessage().contains("Unknown Index name")) {
                    print("Index 'data' does not exist. Skipping drop operation.");
                } else {
                    throw e;
                }
            }

            for (Map.Entry<String, String> entry : values.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                jedis.jsonSet(key, new Path2("$"), value);
            }

            createRedisIndex(jedis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();

        print("Carregamento dados Redis: " + (endTime - startTime) / 1_000_000);
    }

    private static void postgresLoadData(HashMap<String, String> values) {
        long startTime = System.nanoTime();
        print("Loading data for Postgres");
        try (Connection conn = DriverManager.getConnection(POSTGRES_URL, POSTGRES_USER, POSTGRES_PASSWORD)) {

            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM test_table")) {
                stmt.executeUpdate();

            }
            for (HashMap.Entry<String, String> value : values.entrySet()) {
                try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO test_table (data) VALUES (?)")) {
                    stmt.setString(1, value.getValue());
                    stmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        print("Carregamento dados Postgres: " + (endTime - startTime) / 1_000_000);
    }

    public static void performFullTextSearch(String searchTerm) {
        print("Performing full-text search for term: " + searchTerm);

        print("********* REDIS **********");
        try {
            long redisTime = searchInRedis(searchTerm);
            print("Redis search time: " + redisTime + "ms");
        } catch (Exception e) {
            print("Redis search failed: " + e.getMessage());
        }
        print("********* **********");
        print("********* POSTGRES **********");
        try {
            long postgresTime = searchInPostgres(searchTerm);
            print("PostgreSQL search time: " + postgresTime + "ms");
        } catch (Exception e) {
            print("PostgreSQL search failed: " + e.getMessage());
        }
        print("********* **********");
    }

    private static long searchInRedis(String searchTerm) throws Exception {
        long startTime = System.nanoTime();

        try (UnifiedJedis jedis = new UnifiedJedis("http://" + REDIS_HOST + ":" + REDIS_PORT)) {
            SearchResult result = jedis.ftSearch("data", searchTerm);
            print("Redis search results: " + result.getDocuments());
        } catch (JedisDataException e) {
            print("Redis search failed: " + e.getMessage());
            throw e;
        }

        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    private static void createRedisIndex(UnifiedJedis jedis) throws Exception {
        try {
            Schema schema = new Schema()
                    .addTextField("$.country", 1.0).as("country")
                    .addTextField("$.city", 1.0).as("city")
                    .addTextField("$.street", 1.0).as("street")
                    .addTextField("$.name", 1.0).as("name");

            IndexDefinition def = new IndexDefinition(IndexDefinition.Type.JSON).setPrefixes(new String[]{"data:"});

            jedis.ftCreate("data", IndexOptions.defaultOptions().setDefinition(def), schema);
        } catch (JedisDataException e) {
                throw e;
        }
    }

    private static long searchInPostgres(String searchTerm) throws Exception {
        long startTime = System.nanoTime();

        String query = "SELECT data FROM test_table WHERE data LIKE ?";

        try (Connection conn = DriverManager.getConnection(POSTGRES_URL, POSTGRES_USER, POSTGRES_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + searchTerm + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                int count = 0;
                List<String> results = new ArrayList<>();
                while (rs.next()) {
                    results.add(rs.getString("data"));
                    count++;
                }
//                print("PostgreSQL search results: " + results);
            }
        }

        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
    }

    private static void print(Object value) {
        System.out.println(value);
    }
}
