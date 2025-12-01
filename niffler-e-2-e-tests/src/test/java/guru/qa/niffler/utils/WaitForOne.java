package guru.qa.niffler.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class WaitForOne<K, V> {

  private final Map<K, SyncSubject> storedValues = new ConcurrentHashMap<>();

  public void put(@Nonnull K k, @Nonnull V v) {
    storedValues.computeIfAbsent(k, SyncSubject::new)
        .putOnce(v);
  }

  @Nullable
  public V get(@Nonnull K k, long timeoutMs) {
    SyncSubject subject = storedValues.computeIfAbsent(k, SyncSubject::new);
    try {
      return subject.latch.await(timeoutMs, TimeUnit.MILLISECONDS)
          ? subject.value
          : null;
    } catch (InterruptedException e) {
      return null;
    }
  }

  private final class SyncSubject {
    private final CountDownLatch latch;
    private final K key;
    private V value;

    private SyncSubject(K key) {
      this.key = key;
      this.latch = new CountDownLatch(1);
    }

    private synchronized void putOnce(V v) {
      if (this.latch.getCount() != 0L) {
        this.value = v;
        this.latch.countDown();
      }
    }
  }
}
