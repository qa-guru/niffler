package guru.qa.niffler.page.component;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.SpendRustamCondition.spends;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import guru.qa.niffler.model.Spend7Json;
import io.qameta.allure.Step;

public class SpendingRustamTable extends BaseComponentRustam<SpendingRustamTable> {

    private final ElementsCollection spendingButtons = self.$$(".spendings__buttons button");

    public SpendingRustamTable() {
        super($(".main-content__section-history"));
    }

    @Step("Click by button {0}")
    public SpendingRustamTable clickByButton(String buttonText) {
        spendingButtons.find(Condition.text(buttonText))
                .scrollIntoView(true)
                .click();
        return this;
    }

    @Step("Check that table contains data {0}")
    public SpendingRustamTable checkTableContains(Spend7Json... expectedSpends) {
        self.$$("tbody tr").should(spends(expectedSpends));
        return this;
    }


    public SpendingRustamTable addSpending() {
        return this;
    }

    public SpendingRustamTable checkTable() {
        return this;
    }

    public SpendingRustamTable deleteSpending() {
        return this;
    }

    @Override
    public SpendingRustamTable checkThatComponentDisplayed() {
        return null;
    }
}
