package niffler.page.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Selenide.$;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Calendar extends BaseComponent<Calendar> {

    public Calendar(SelenideElement self) {
        super(self);
    }

    /**
     * Select year, day and month in calendar
     *
     * @param date - date in format "dd/MM/yyyy" | "dd-MM-yyyy" | "yyyy/MM/dd" | "yyyy-MM-dd"
     */
    @Step("Select date in calendar: {date}")
    public void selectDateInCalendar(String date) {
        String[] allowedFormats = {"dd/MM/yyyy", "dd-MM-yyyy", "yyyy/MM/dd", "yyyy-MM-dd"};
        Date parsedDate = null;
        java.util.Calendar cal = new GregorianCalendar();
        for (String formatString : allowedFormats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(formatString);
                sdf.setLenient(false);
                parsedDate = sdf.parse(date);
                break;
            } catch (ParseException e) {
                continue;
            }
        }
        assertNotNull(parsedDate, "Expected date format: \"dd/MM/yyyy\", \"dd-MM-yyyy\", \"yyyy/MM/dd\", \"yyyy-MM-dd\", but actual date: " + date);
        cal.setTime(parsedDate);

        $(".react-datepicker__input-container input").click();
        self.should(Condition.visible);

        selectYear(cal.get(YEAR));
        selectMonth(cal.get(MONTH) + 1);
        selectDay(cal.get(DAY_OF_MONTH));
    }

    /**
     * Select year in calendar
     *
     * @param expectedYear - expected year
     */
    protected void selectYear(int expectedYear) {
        int actualYear = getActualYear();

        if (actualYear > expectedYear) {
            while (actualYear > expectedYear) {
                clickPrev();
                Selenide.sleep(500);
                actualYear = getActualYear();
            }
        } else if (actualYear < expectedYear) {
            while (actualYear < expectedYear) {
                clickNext();
                Selenide.sleep(500);
                actualYear = getActualYear();
            }
        }
    }

    /**
     * Select month in calendar, for already selected year
     *
     * @param expectedMonth - expected month, for example: 0 - january, 11 - december;
     *                      <p>
     *                      NOTE: pika-lendar component was represent months for starting at 0 to.
     *                      </p>
     */
    protected void selectMonth(int expectedMonth) {
        int actualMonth = getActualMonth();

        if (actualMonth > (expectedMonth)) {
            while (actualMonth > (expectedMonth)) {
                clickPrev();
                Selenide.sleep(500);
                actualMonth = getActualMonth();
            }
        } else if (actualMonth < (expectedMonth)) {
            while (actualMonth < (expectedMonth)) {
                clickNext();
                Selenide.sleep(500);
                actualMonth = getActualMonth();
            }
        }
    }

    /**
     * Select day in calendar, for already selected year and month
     *
     * @param expectedDay - expected day
     */
    protected void selectDay(int expectedDay) {
        ElementsCollection rows = self
                .findAll(".react-datepicker__week");

        for (SelenideElement row : rows) {
            ElementsCollection days = row.$$(".react-datepicker__day");
            for (SelenideElement day : days) {
                if (Integer.parseInt(day.getText()) == expectedDay) {
                    day.click();
                    return;
                }
            }
        }
    }

    protected int getActualMonth() {
        return Month.valueOf(self.$(".react-datepicker__current-month")
                        .getText()
                        .split(" ")[0]
                        .toUpperCase())
                .getValue();
    }

    protected int getActualYear() {
        return Integer.parseInt(self.$(".react-datepicker__current-month")
                .getText()
                .split(" ")[1]);
    }

    protected void clickNext() {
        self.$(".react-datepicker__navigation--next").click();
    }

    protected void clickPrev() {
        self.$(".react-datepicker__navigation--previous").click();
    }
}
