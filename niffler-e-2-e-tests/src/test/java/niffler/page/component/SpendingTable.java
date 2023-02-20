package niffler.page.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;
import niffler.model.SpendJson;

import static com.codeborne.selenide.Selenide.$;
import static niffler.condition.SpendCondition.spends;

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

    public SpendingTable addSpending() {
        return this;
    }

    public SpendingTable checkTable() {
        return this;
    }

    public SpendingTable deleteSpending() {
        return this;
    }
}
