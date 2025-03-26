package benchmarks.keyvalue.tests;

import benchmarks.core.BaseClass;
import redis.clients.jedis.Jedis;
import utils.ConfigUtils;
import utils.annotations.ConfigProperty;

public class RedisOperations extends BaseClass {


    public static String redisHost = "127.0.0.1";

    public static int redisPort = 6379;

    public RedisOperations() {
    }

    public static long read(int iteration_size) {
        long startTime = System.nanoTime();
        try (Jedis jedis = new Jedis(redisHost, redisPort)) {
            for (int i = 0; i < iteration_size; i++) {
                jedis.get("key" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();

        return  (endTime - startTime) / 1_000_000;
    }

    public static long write(int iteration_size) {
        long startTime = System.nanoTime();
        try (Jedis jedis = new Jedis(redisHost, redisPort)) {
            for (int i = 0; i < iteration_size; i++) {
                jedis.set("key" + i, "TestData" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        return  (endTime - startTime) / 1_000_000;
    }
}
