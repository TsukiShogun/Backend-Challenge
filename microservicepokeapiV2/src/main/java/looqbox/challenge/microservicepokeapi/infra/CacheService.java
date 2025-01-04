package looqbox.challenge.microservicepokeapi.infra;

import looqbox.challenge.microservicepokeapi.exception.NullKeyException;
import looqbox.challenge.microservicepokeapi.exception.NullValueException;
import looqbox.challenge.microservicepokeapi.exception.CacheCleanupException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class CacheService<K, V> {

    private final Map<K, CacheItem<V>> cacheMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public CacheService() {
        executorService.scheduleAtFixedRate(this::cleanUp, 1, 1, TimeUnit.MINUTES);
    }

    public void put(K key, V value, long ttl) {
        if (key == null) {
            throw new NullKeyException("Key cannot be null.");
        }
        if (value == null) {
            throw new NullValueException("Value cannot be null.");
        }
        cacheMap.put(key, new CacheItem<>(value, ttl));
    }

    public V get(K key) {
        if (key == null) {
            throw new NullKeyException("Key cannot be null.");
        }

        CacheItem<V> item = cacheMap.get(key);
        if (item == null || item.isExpired()) {
            cacheMap.remove(key);
            return null;
        }
        return item.value;
    }

    private void cleanUp() {
        try {
            cacheMap.entrySet().removeIf(entry -> entry.getValue().isExpired());
        } catch (Exception e) {
            throw new CacheCleanupException("Error cleaning up the cache.");
        }
    }


    private static class CacheItem<V> {
        final V value;
        final long expiryTime;

        CacheItem(V value, long ttl) {
            this.value = value;
            this.expiryTime = System.currentTimeMillis() + ttl;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }
}
