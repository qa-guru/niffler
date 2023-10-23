package guru.qa.niffler.page.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PeopleTable extends BaseComponent<PeopleTable> {

    public PeopleTable(SelenideElement self) {
        super(self);
    }

    @Nonnull
    public ElementsCollection getAllRows() {
        return $$("tbody tr");
    }

    @Nonnull
    public SelenideElement getRowByUsername(@Nonnull String username) {
        ElementsCollection allRows = getAllRows();
        SelenideElement table = $(".table");
        table.shouldBe(Condition.visible);
        return allRows.find(text(username));
    }

    @Nonnull
    public SelenideElement getUsernameCell(@Nonnull SelenideElement row) {
        return row.$$("td").get(1);
    }

    @Nonnull
    public SelenideElement getActionsCell(@Nonnull SelenideElement row) {
        return row.$$("td").get(3);
    }
}
