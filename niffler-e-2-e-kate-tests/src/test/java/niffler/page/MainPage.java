package niffler.page;

import niffler.model.SpendModel;
import com.codeborne.selenide.SelenideElement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Condition.text;

public class MainPage {
    DateFormat spendingTableDateFormat = new SimpleDateFormat("d MMM yy");

    public SelenideElement spendingTableBody() {
        return $(".table.spendings-table").$("tbody");
    }

    public void checkSpendPresence(SpendModel spend) {
        spendingTableBody().shouldHave(
                text(spend.getCategory()),
                text(spend.getDescription()),
                text(spend.getCurrency().toString()),
                text(spendingTableDateFormat.format(spend.getSpendDate())),
                text(String.valueOf(spend.getAmount())));
    }
}
