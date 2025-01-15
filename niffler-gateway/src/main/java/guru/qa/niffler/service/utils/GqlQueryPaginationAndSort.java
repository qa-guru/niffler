package guru.qa.niffler.service.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class GqlQueryPaginationAndSort {
  private final int page;
  private final int size;
  private final @Nullable List<String> sort;

  public GqlQueryPaginationAndSort(int page, int size, @Nullable List<String> sort) {
    this.page = page;
    this.size = size;
    this.sort = sort;
  }

  public @Nonnull Pageable pageable() {
    return PageRequest.of(
        page,
        size,
        sort()
    );
  }

  private Sort sort() {
    if (sort != null) {
      return Sort.by(
          sort.stream().map(s ->
              new Sort.Order(
                  Sort.Direction.valueOf(s.split(",")[1]),
                  s.split(",")[0]
              )).toList()
      );
    }
    return Sort.unsorted();
  }
}
