package guru.qa.niffler.soap;

import jakarta.annotation.Nonnull;
import jaxb.userdata.PageInfo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class SpringPageable {
  private final PageInfo pageInfo;

  public SpringPageable(PageInfo pageInfo) {
    this.pageInfo = pageInfo;
  }

  public @Nonnull Pageable pageable() {
    return PageRequest.of(
        pageInfo.getPage(), pageInfo.getSize(), sortFromRequest()
    );
  }

  private @Nonnull Sort sortFromRequest() {
    return Sort.by(pageInfo.getSort().stream()
        .map(st -> new Sort.Order(
                Sort.Direction.fromString(
                    st.getDirection().name()
                ),
                st.getProperty()
            )
        ).toList());
  }
}
