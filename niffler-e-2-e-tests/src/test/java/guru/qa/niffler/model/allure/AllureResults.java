package guru.qa.niffler.model.allure;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record AllureResults(@JsonProperty("results") List<DecodedAllureFile> results) {
}
