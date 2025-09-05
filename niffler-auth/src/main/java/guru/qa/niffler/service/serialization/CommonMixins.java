package guru.qa.niffler.service.serialization;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.web.servlet.FlashMap;

import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class CommonMixins {

  @JsonSerialize(as = CopyOnWriteArrayList.class)
  @JsonDeserialize(as = CopyOnWriteArrayList.class)
  public static abstract class CopyOnWriteArrayListMixin<T> {
  }

  @JsonSerialize(as = FlashMap.class)
  @JsonDeserialize(as = FlashMap.class)
  public static abstract class FlashMapMixin<T> {
  }

  @JsonSerialize(as = HashSet.class)
  @JsonDeserialize(as = HashSet.class)
  public static abstract class HashSetMixin<T> {
  }
}
