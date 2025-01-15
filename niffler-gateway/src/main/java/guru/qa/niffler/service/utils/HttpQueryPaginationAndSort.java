package guru.qa.niffler.service.utils;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.annotation.Nonnull;

public class HttpQueryPaginationAndSort {
  private final Pageable pageable;

  public HttpQueryPaginationAndSort(@Nonnull Pageable pageable) {
    this.pageable = pageable;
  }

  public @Nonnull String string() {
    StringBuilder query = new StringBuilder();
    query.append("&page=")
        .append(pageable.getPageNumber())
        .append("&size=")
        .append(pageable.getPageSize());

    if (!pageable.getSort().isEmpty()) {
      for (Sort.Order order : pageable.getSort()) {
        query.append("&sort=")
            .append(order.getProperty())
            .append(",")
            .append(order.getDirection().name());
      }
    }
    return query.toString();
  }

  @Override
  public String toString() {
    return string();
  }
}
