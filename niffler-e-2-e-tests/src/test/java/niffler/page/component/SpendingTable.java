package niffler.page.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class SpendingTable extends BaseComponent<SpendingTable> {

    private final ElementsCollection spendingButtons = self.$$(".spendings__buttons");

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
    public SpendingTable checkTableContains(String... expectedDescriptions) {
        ElementsCollection rows = self.$$("tr");
        for (String description : expectedDescriptions) {
            rows.find(Condition.text(description))
                    .$$("td").get(5)
                    .should(Condition.text(description));
        }
        return this;
    }
}
