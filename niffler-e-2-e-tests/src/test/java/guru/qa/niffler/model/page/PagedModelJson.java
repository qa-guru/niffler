package guru.qa.niffler.model.page;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;

import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;

@JsonIgnoreProperties(ignoreUnknown = true)
@ParametersAreNonnullByDefault
public class PagedModelJson<T> extends PagedModel<T> {

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public PagedModelJson(@JsonProperty("content") List<T> content,
                        @JsonProperty("page") PageMetadata page) {
    super(
        new PageImpl<>(
            content,
            PageRequest.of(
                (int) page.number(),
                (int) page.size()
            ),
            page.totalElements()
        )
    );
  }
}
