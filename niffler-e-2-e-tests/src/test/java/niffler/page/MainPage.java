package niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;
import org.opentest4j.AssertionFailedError;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage extends BasePage<MainPage> {

    private final ElementsCollection spendingButtons = $$(".spendings__buttons");
    private final SelenideElement spendingTable = $(".spendings-table");

    public MainPage clickByButton(String buttonText) {
        spendingButtons.find(Condition.text(buttonText))
                .scrollIntoView(false)
                .click();
        return this;
    }

    public MainPage checkTableContains(String... expectedDescriptions) {
        ElementsCollection rows = spendingTable.$$("tr");
        for (String description : expectedDescriptions) {
            rows.find(Condition.text(description))
                    .$$("td").get(5)
                    .should(Condition.text(description));
        }
        return this;
    }

}
