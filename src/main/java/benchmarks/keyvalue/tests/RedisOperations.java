package benchmarks.keyvalue.tests;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class RedisOperations {

    public RedisOperations() {
    }

    public static long read(Jedis jedisConn, int iteration_size) {
        long startTime = System.nanoTime();
        try {
            Pipeline redisPipeline = jedisConn.pipelined();
            for (int i = 0; i < iteration_size; i++) {
                redisPipeline.get("key" + i);
            }
            redisPipeline.sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();

        return  (endTime - startTime) / 1000000;
    }

    public static long write(Jedis jedisConn, int iteration_size) {
        long startTime = System.nanoTime();
        try {
            for (int i = 0; i < iteration_size; i++) {
                jedisConn.set("key" + i, "TestData" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        return  (endTime - startTime) / 1000000;
    }
}
