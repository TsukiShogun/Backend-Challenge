package looqbox.challenge.microservicepokeapi.infra;

import looqbox.challenge.microservicepokeapi.exception.NullKeyException;
import looqbox.challenge.microservicepokeapi.exception.NullValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CacheComponetTest {

    @InjectMocks
    private CacheComponet<String, String> cacheService;

    @Test
    @DisplayName("should store a value in cache with TTL")
    void put_shouldStoreValueWithTTL() {

        String key = "pokemon";
        String value = "pikachu";
        long ttl = TimeUnit.MINUTES.toMillis(1);


        cacheService.put(key, value, ttl);
        String cachedValue = cacheService.get(key);


        assertNotNull(cachedValue);
        assertEquals("pikachu", cachedValue);
    }

    @Test
    @DisplayName("should throw NullKeyException when key is null")
    void put_shouldThrowNullKeyExceptionWhenKeyIsNull() {

        assertThrows(NullKeyException.class, () -> cacheService.put(null, "value", 1000));
    }

    @Test
    @DisplayName("should throw NullValueException when value is null")
    void put_shouldThrowNullValueExceptionWhenValueIsNull() {

        assertThrows(NullValueException.class, () -> cacheService.put("key", null, 1000));
    }



    @Nested
    @DisplayName("Handle cache with null or empty values")
    class NullOrEmptyValuesTests {

        @Test
        @DisplayName("should throw NullKeyException when key is null")
        void shouldThrowNullKeyExceptionWhenKeyIsNull() {
            assertThrows(NullKeyException.class, () -> cacheService.get(null));
        }

        @Test
        @DisplayName("should throw NullValueException when value is null during put")
        void shouldThrowNullValueExceptionWhenValueIsNull() {
            assertThrows(NullValueException.class, () -> cacheService.put("key", null, 1000));
        }
    }

}