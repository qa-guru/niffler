package niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Month;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Selenide.$;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class Calendar extends BaseComponent<Calendar> {

    public Calendar(SelenideElement self) {
        super(self);
    }

    private final SelenideElement input = $(".react-datepicker__input-container input");
    private final SelenideElement prevButton = self.$(".react-datepicker__navigation--previous");
    private final SelenideElement nextButton = self.$(".react-datepicker__navigation--next");
    private final SelenideElement currentMonthAndYear = self.$(".react-datepicker__current-month");

    @Step("Select date in calendar: {date}")
    public void selectDateInCalendar(Date date) {
        java.util.Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        input.click();
        selectYear(cal.get(YEAR));
        selectMonth(cal.get(MONTH));
        selectDay(cal.get(DAY_OF_MONTH));
    }

    private void selectYear(int expectedYear) {
        int actualYear = getActualYear();

        while (actualYear > expectedYear) {
            prevButton.click();
            Selenide.sleep(200);
            actualYear = getActualYear();
        }
        while (actualYear < expectedYear) {
            nextButton.click();
            Selenide.sleep(200);
            actualYear = getActualYear();
        }
    }

    private void selectMonth(int desiredMonth) {
        int actualMonth = getActualMonth();

        while (actualMonth > desiredMonth) {
            prevButton.click();
            Selenide.sleep(200);
            actualMonth = getActualMonth();
        }
        while (actualMonth < desiredMonth) {
            nextButton.click();
            Selenide.sleep(200);
            actualMonth = getActualMonth();
        }
    }

    private void selectDay(int desiredDay) {
        ElementsCollection rows = self.findAll(".react-datepicker__week").snapshot();

        for (SelenideElement row : rows) {
            ElementsCollection days = row.$$(".react-datepicker__day").snapshot();
            for (SelenideElement day : days) {
                if (Integer.parseInt(day.getText()) == desiredDay) {
                    day.click();
                    return;
                }
            }
        }
    }

    private int getActualMonth() {
        return Month.valueOf(currentMonthAndYear.getText()
                        .split(" ")[0]
                        .toUpperCase())
                .ordinal();
    }

    private int getActualYear() {
        return Integer.parseInt(currentMonthAndYear.getText().split(" ")[1]);
    }
}
