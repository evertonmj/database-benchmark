package benchmarks.keyvalue;

import benchmarks.keyvalue.tests.MemcachedOperations;
import benchmarks.keyvalue.tests.RedisOperations;
import net.spy.memcached.MemcachedClient;
import org.openjdk.jmh.annotations.*;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
public class KeyValueBenchmark {

    private Jedis jedis;
    private MemcachedClient memcachedClient;
    private static final int ITERATIONS = 100000;

    @Setup(Level.Trial)
    public void setup() throws IOException {
        jedis = new Jedis("127.0.0.1", 6379);
        memcachedClient = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));

        // Warmup (optional)
        RedisOperations.write(jedis, ITERATIONS);
        MemcachedOperations.write(memcachedClient, ITERATIONS);
    }

    @TearDown(Level.Trial)
    public void teardown() {
        jedis.close();
        memcachedClient.shutdown();
    }

    @Benchmark
    public void redisWriteBenchmark() {
        RedisOperations.write(jedis, ITERATIONS);
    }

    @Benchmark
    public void memcachedWriteBenchmark() throws IOException {
        MemcachedOperations.write(memcachedClient, ITERATIONS);
    }

    @Benchmark
    public void redisReadBenchmark() {
        RedisOperations.read(jedis, ITERATIONS);
    }

    @Benchmark
    public void memcachedReadBenchmark() throws IOException {
        MemcachedOperations.read(memcachedClient, ITERATIONS);
    }
}
