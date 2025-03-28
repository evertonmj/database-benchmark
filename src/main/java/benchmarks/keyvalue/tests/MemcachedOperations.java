package benchmarks.keyvalue.tests;

import net.spy.memcached.MemcachedClient;

import java.io.IOException;

public class MemcachedOperations {

    public static long write(MemcachedClient memcachedClient, int iterationSize) throws IOException {
        long startTime = System.nanoTime();
        try {
            for (int i = 0; i < iterationSize; i++) {
                memcachedClient.set("key" + i, 0, "TestData" + i).get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000000;
    }

    public static long read(MemcachedClient memcachedClient, int iterationSize) throws IOException {
        long startTime = System.nanoTime();
        try {
            for (int i = 0; i < iterationSize; i++) {
                memcachedClient.get("key" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000000;

    }
}
