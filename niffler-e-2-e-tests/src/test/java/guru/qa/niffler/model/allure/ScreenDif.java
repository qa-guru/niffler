package guru.qa.niffler.model.allure;

import javax.annotation.ParametersAreNonnullByDefault;
@ParametersAreNonnullByDefault
public record ScreenDif(String expected, String actual, String diff) {
}
