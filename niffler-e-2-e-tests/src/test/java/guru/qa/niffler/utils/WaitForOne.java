package guru.qa.niffler.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class WaitForOne<K, V> {

    private final Map<K, SyncSubject> storedValues = new ConcurrentHashMap<>();

    public void provide(@Nonnull K k, @Nonnull V v) {
        storedValues.computeIfAbsent(k, SyncSubject::new)
                .provideIfNotProvided(v);
    }

    @Nullable
    public V wait(@Nonnull K k, long timeoutMs) {
        SyncSubject subject = storedValues.computeIfAbsent(k, SyncSubject::new);
        try {
            return subject.latch.await(timeoutMs, TimeUnit.MILLISECONDS)
                    ? subject.value
                    : null;
        } catch (InterruptedException e) {
            return null;
        }
    }

    @Nonnull
    public Map<K, V> getAsMap() {
        return this.storedValues.entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                (v) -> v.getValue().value
                        ));
    }

    private final class SyncSubject {
        private final CountDownLatch latch;
        private final K key;
        private V value;

        private SyncSubject(K key) {
            this.key = key;
            this.latch = new CountDownLatch(1);
        }

        private synchronized void provideIfNotProvided(V v) {
            if (this.latch.getCount() != 0L) {
                this.value = v;
                this.latch.countDown();
            }
        }
    }
}
