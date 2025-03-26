import benchmarks.keyvalue.tests.MemcachedOperations;
import benchmarks.keyvalue.tests.RedisOperations;
import net.spy.memcached.MemcachedClient;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.InetSocketAddress;

public class DatabaseBenchmarkApplication {

    public static void main(String[] args) throws IOException {
        Jedis jedisConn = new Jedis("127.0.0.1", 6379);

        MemcachedClient memcacheConn = new MemcachedClient(
                new InetSocketAddress("127.0.0.1", 11211)
        );

        int testSize = 100000;
        System.out.println("Starting KeyValue Tests");
        System.out.println("Redis - Writing "+testSize+" key-value pairs");
        long writeTimeRedis = RedisOperations.write(jedisConn, testSize);
        System.out.println("Redis - Reading "+testSize+" key-value pairs");
        long readTimeRedis = RedisOperations.read(jedisConn, testSize);

        System.out.println("REDIS - KeyValue Tests");
        System.out.println("Write Time: " + writeTimeRedis + " ms");
        System.out.println("Read Time: " + readTimeRedis + " ms");


        System.out.println("Memcached - Writing "+testSize+" key-value pairs");
        long writeTimeMemcached = MemcachedOperations.write(memcacheConn, testSize);
        System.out.println("Memcached - Reading "+testSize+" key-value pairs");
        long readTimeMemcached = MemcachedOperations.read(memcacheConn, testSize);

        System.out.println("Memcached - KeyValue Tests");
        System.out.println("Write Time: " + writeTimeMemcached + " ms");
        System.out.println("Read Time: " + readTimeMemcached + " ms");

    }
}