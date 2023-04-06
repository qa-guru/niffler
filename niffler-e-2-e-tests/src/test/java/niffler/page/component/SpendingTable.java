package niffler.page.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import niffler.model.rest.SpendJson;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static niffler.condition.spend.SpendCondition.spends;

public class SpendingTable extends BaseComponent<SpendingTable> {

    private final ElementsCollection spendingButtons = self.$$(".spendings__buttons button");

    public SpendingTable() {
        super($(".main-content__section-history"));
    }

    @Step("Click by button {0}")
    public SpendingTable clickByButton(String buttonText) {
        spendingButtons.find(Condition.text(buttonText))
                .scrollIntoView(false)
                .click();
        return this;
    }

    @Step("Check that table contains data {0}")
    public SpendingTable checkTableContains(SpendJson... expectedSpends) {
        self.$$("tbody tr").should(spends(expectedSpends));
        return this;
    }

    public SpendingTable editSpending(int row, SpendJson editedSpending) {
        SelenideElement rowElement = self.$$("tbody tr").get(row);
        rowElement.$(".button-icon_type_edit").click();
        setSpendingAmount(rowElement, editedSpending.getAmount());
        setSpendingDescription(rowElement, editedSpending.getDescription());
        setSpendingCategory(rowElement, editedSpending.getCategory());
//        setSpendingDate(rowElement, editedSpending.getSpendDate());
        submitEditSpending(rowElement);
        return this;
    }

    public SpendingTable deleteSpending() {
        return this;
    }

    private void setSpendingAmount(SelenideElement row, Double amount) {
        row.$("input[name=amount]").setValue(String.valueOf(amount));
    }

    private void setSpendingDescription(SelenideElement row, String description) {
        row.$("input[name=description]").setValue(String.valueOf(description));
    }

    private void setSpendingDate(SelenideElement row, Date date) {
        SelenideElement input = row.$(".react-datepicker-wrapper #editable__input");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        input.clear();
        input.setValue(dateFormat.format(date));
        self.click();
        self.scrollIntoView(false);
    }

    private void setSpendingCategory(SelenideElement row, String category) {
        row.$("input[id^='react-select']").scrollIntoView(false).setValue(category);
        row.$$("div[id^='react-select']").find(exactText(category)).click();
    }

    private void submitEditSpending(SelenideElement row) {
        row.$(".button-icon_type_submit").click();
    }
}
