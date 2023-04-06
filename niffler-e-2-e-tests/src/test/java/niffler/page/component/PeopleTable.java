package niffler.page.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PeopleTable extends BaseComponent<PeopleTable> {

    public PeopleTable(SelenideElement self) {
        super(self);
    }

    public ElementsCollection getAllRows() {
        return $$("tbody tr");
    }

    public SelenideElement getRowByUsername(String username) {
        ElementsCollection allRows = getAllRows();
        SelenideElement table = $(".table");
        String s = table.innerHtml();
        table.shouldBe(Condition.visible);
        return allRows
                .stream()
                .filter(r -> username.equals(getUsernameCell(r)))
                .findFirst()
                .orElseThrow();
    }

    public SelenideElement getUsernameCell(SelenideElement row) {
        return row.$$("td").get(1);
    }

    public SelenideElement getActionsCell(SelenideElement row) {
        return row.$$("td").get(3);
    }
}
