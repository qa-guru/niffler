package guru.qa.niffler.model.allure;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record DecodedAllureFile(@JsonProperty("file_name") String fileName,
                                @JsonProperty("content_base64") String contentBase64) {
}
