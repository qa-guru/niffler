package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.ObjectCondition;
import com.codeborne.selenide.Selenide;
import com.google.common.base.Joiner;
import org.openqa.selenium.WebDriver;

import javax.annotation.Nonnull;
import java.util.Map;

public class LocalStorageCondition {

  @Nonnull
  public static ObjectCondition<WebDriver> localStorageItem(String itemKey) {
    return new LocalStorage(itemKey);
  }

  private record LocalStorage(String expectedItem) implements ObjectCondition<WebDriver> {

    @Override
    public String description() {
      return String.format("should have a local storage item with key \"%s\"", expectedItem);
    }

    @Override
    public String negativeDescription() {
      return String.format("should not have local storage with key \"%s\"", expectedItem);
    }

    @Override
    public CheckResult check(WebDriver webDriver) {
      final Map<String, String> localStorageItems = Selenide.localStorage().getItems();
      final String value = localStorageItems.get(expectedItem);
      return result(webDriver, value != null, actualValue(localStorageItems));
    }

    private String actualValue(Map<String, String> items) {
      return String.format("Available values: %s", Joiner.on(",").withKeyValueSeparator("=").join(items));
    }

    @Override
    public String expectedValue() {
      return String.format("Local storage item with key \"%s\"", expectedItem);
    }

    @Override
    public String describe(WebDriver webDriver) {
      return "webdriver";
    }
  }
}
