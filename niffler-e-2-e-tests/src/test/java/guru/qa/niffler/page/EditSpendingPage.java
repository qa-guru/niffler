package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.page.component.Calendar;
import guru.qa.niffler.page.component.SelectField;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

import java.util.Date;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class EditSpendingPage extends BasePage<EditSpendingPage> {

  public static final String URL = CFG.frontUrl() + "spending";

  private final Calendar calendar = new Calendar();
  private final SelectField currencySelect = new SelectField($("#currency"));

  private final SelenideElement amountInput = $("#amount");
  private final SelenideElement categoryInput = $("input[name='category']");
  private final ElementsCollection categories = $$(".MuiChip-root");
  private final SelenideElement descriptionInput = $("#description");

  private final SelenideElement cancelBtn = $(".MuiGrid-root button[type='button']");
  private final SelenideElement saveBtn = $(".MuiGrid-root button[type='submit']");

  @Override
  public EditSpendingPage waitForPageLoaded() {
    amountInput.should(visible);
    return this;
  }

  @Step("Fill spending data from object")
  public EditSpendingPage fillPage(SpendJson spend) {
    return setNewSpendingDate(spend.spendDate())
        .setNewSpendingAmount(spend.amount())
        .setNewSpendingCurrency(spend.currency())
        .setNewSpendingCategory(spend.category().name())
        .setNewSpendingDescription(spend.description());
  }

  @Step("Select new spending currency: {0}")
  public EditSpendingPage setNewSpendingCurrency(CurrencyValues currency) {
    currencySelect.setValue(currency.name());
    return this;
  }

  @Step("Select new spending category: {0}")
  public EditSpendingPage setNewSpendingCategory(String category) {
    clearField(categoryInput);
    categoryInput.setValue(category);
    return this;
  }

  @Step("Set new spending amount: {0}")
  public EditSpendingPage setNewSpendingAmount(double amount) {
    clearField(amountInput);
    amountInput.setValue(String.valueOf(amount));
    return this;
  }

  @Step("Set new spending amount: {0}")
  public EditSpendingPage setNewSpendingAmount(int amount) {
    clearField(amountInput);
    amountInput.setValue(String.valueOf(amount));
    return this;
  }

  @Step("Set new spending date: {0}")
  public EditSpendingPage setNewSpendingDate(Date date) {
    calendar.selectDateInCalendar(date);
    return this;
  }

  @Step("Set new spending description: {0}")
  public EditSpendingPage setNewSpendingDescription(String description) {
    clearField(descriptionInput);
    descriptionInput.setValue(description);
    return this;
  }

  @Step("Click submit button to create new spending")
  public EditSpendingPage saveSpending() {
    saveBtn.click();
    return this;
  }

  private void clearField(SelenideElement field) {
    field.click();
    while (!Objects.equals(field.getValue(), "")) {
      field.sendKeys(Keys.BACK_SPACE);
    }
  }
}
