package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public abstract class BasePage<T extends BasePage<?>> {

  protected static final Config CFG = Config.getConfig();

  private final SelenideElement alert = $(".MuiSnackbar-root");
  private final ElementsCollection formErrors = $$("p.Mui-error");

  public abstract T waitForPageLoaded();

  @Step("Check that alert message appears: {expectedText}")
  @SuppressWarnings("unchecked")
  public T checkAlertMessage(String expectedText) {
    alert.should(Condition.visible).should(Condition.text(expectedText));
    return (T) this;
  }

  @Step("Check that form error message appears: {expectedText}")
  @SuppressWarnings("unchecked")
  public T checkFormErrorMessage(String... expectedText) {
    formErrors.should(CollectionCondition.textsInAnyOrder(expectedText));
    return (T) this;
  }
}
