package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.db.entity.userdata.CurrencyValues;
import guru.qa.niffler.model.Spend7Json;
import guru.qa.niffler.utils.DateUtils;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SpendRustamCondition2 {

  public static CollectionCondition spends(Spend7Json... expectedSpends) {
    return new CollectionCondition() {

      @Nonnull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        return super.check(driver, elements);
      }

      @Override
      public void fail(CollectionSource collection, CheckResult lastCheckResult,
          @Nullable Exception cause, long timeoutMs) {
        super.fail(collection, lastCheckResult, cause, timeoutMs);
      }

      @Override
      public boolean missingElementSatisfiesCondition() {
        return false;
      }
    };
  }
}
