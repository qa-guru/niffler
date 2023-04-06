package niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import niffler.page.component.Calendar;
import niffler.page.component.Footer;
import niffler.page.component.Header;
import niffler.page.component.Select;
import niffler.page.component.SpendingTable;

import java.util.Date;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BasePage<MainPage> {

    public static final String URL = CFG.frontUrl() + "main";

    protected final Header header = new Header();
    protected final Footer footer = new Footer();
    protected final SpendingTable spendingTable = new SpendingTable();

    private final SelenideElement addSpendingSection = $(".main-content__section-add-spending");
    private final Select categorySelect = new Select(addSpendingSection.$("div.select-wrapper"));
    private final Calendar calendar = new Calendar(addSpendingSection.$(".react-datepicker"));
    private final SelenideElement amountInput = addSpendingSection.$("input[name='amount']");
    private final SelenideElement descriptionInput = addSpendingSection.$("input[name='description']");
    private final SelenideElement submitNewSpendingButton = addSpendingSection.$("button[type='submit']");
    private final SelenideElement errorContainer = addSpendingSection.$(".form__error");

    public Header getHeader() {
        return header;
    }

    public Footer getFooter() {
        return footer;
    }

    public SpendingTable getSpendingTable() {
        spendingTable.getSelf().scrollIntoView(true);
        return spendingTable;
    }

    @Step("Check that page is loaded")
    @Override
    public MainPage waitForPageLoaded() {
        header.getSelf().should(visible).shouldHave(text("Niffler. The coin keeper."));
        footer.getSelf().should(visible).shouldHave(text("Study project for QA Automation Advanced. 2023"));
        spendingTable.getSelf().should(visible).shouldHave(text("History of spendings"));
        return this;
    }

    @Step("Select new spending category: {0}")
    public MainPage setNewSpendingCategory(String category) {
        Selenide.sleep(1000);
        categorySelect.setValue(category);
        return this;
    }

    @Step("Set new spending amount: {0}")
    public MainPage setNewSpendingAmount(int amount) {
        amountInput.setValue(String.valueOf(amount));
        return this;
    }

    @Step("Set new spending amount: {0}")
    public MainPage setNewSpendingAmount(double amount) {
        amountInput.setValue(String.valueOf(amount));
        return this;
    }

    @Step("Set new spending date: {0}")
    public MainPage setNewSpendingDate(Date date) {
        calendar.selectDateInCalendar(date);
        addSpendingSection.$(byText("Add new spending")).click();
        return this;
    }

    @Step("Set new spending description: {0}")
    public MainPage setNewSpendingDescription(String description) {
        descriptionInput.setValue(description);
        return this;
    }

    @Step("Click submit button to create new spending")
    public MainPage submitNewSpending() {
        submitNewSpendingButton.click();
        return this;
    }

    @Step("Check error: {0} is displayed")
    public MainPage checkError(String error) {
        errorContainer.shouldHave(text(error));
        return this;
    }
}
