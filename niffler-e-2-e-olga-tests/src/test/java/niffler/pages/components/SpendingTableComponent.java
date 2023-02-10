package niffler.pages.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import niffler.api.spend.dto.SpendDto;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class SpendingTableComponent extends BaseComponent {

    private final SelenideElement self = $(".spendings__content");

    @Step("Check that spending table contains spending")
    public SpendingTableComponent checkContainsSpending(SpendDto... spends) {
        for (SpendDto spend : spends) {
            self.shouldHave(
                    text(getDateInSpendingTableStyle(spend.getSpendDate())),
                    text(getAmountInSpendingTableStyle(spend.getAmount())),
                    text(spend.getCurrency().toString()),
                    text(spend.getCategory().toString()),
                    text(spend.getDescription()));
        }
        return this;
    }

    private String getAmountInSpendingTableStyle(Double amount) {
        NumberFormat nf = DecimalFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        nf.setGroupingUsed(false);
        return nf.format(amount);
    }

    private String getDateInSpendingTableStyle(Date spendDate) {
        DateFormat dateFormat = new SimpleDateFormat("d MMM yy");
        return dateFormat.format(spendDate);
    }

}
