package guru.qa.niffler.page.component;

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

  public Calendar() {
    super($(".MuiPickersLayout-root"));
  }

  private final SelenideElement input = $("input[name='date']");
  private final SelenideElement calendarButton = $("button[aria-label*='Choose date']");
  private final SelenideElement prevMonthButton = self.$("button[title='Previous month']");
  private final SelenideElement nextMonthButton = self.$("button[title='Next month']");
  private final SelenideElement currentMonthAndYear = self.$(".MuiPickersCalendarHeader-label");
  private final ElementsCollection dateRows = self.$$(".MuiDayCalendar-weekContainer");

  @Step("Select date in calendar: {date}")
  public void selectDateInCalendar(Date date) {
    java.util.Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    calendarButton.click();
    final int desiredMonthIndex = cal.get(MONTH);
    selectYear(cal.get(YEAR));
    selectMonth(desiredMonthIndex);
    selectDay(cal.get(DAY_OF_MONTH));
  }

  private void selectYear(int expectedYear) {
    int actualYear = getActualYear();

    while (actualYear > expectedYear) {
      prevMonthButton.click();
      Selenide.sleep(200);
      actualYear = getActualYear();
    }
    while (actualYear < expectedYear) {
      nextMonthButton.click();
      Selenide.sleep(200);
      actualYear = getActualYear();
    }
  }

  private void selectMonth(int desiredMonthIndex) {
    int actualMonth = getActualMonthIndex();

    while (actualMonth > desiredMonthIndex) {
      prevMonthButton.click();
      Selenide.sleep(200);
      actualMonth = getActualMonthIndex();
    }
    while (actualMonth < desiredMonthIndex) {
      nextMonthButton.click();
      Selenide.sleep(200);
      actualMonth = getActualMonthIndex();
    }
  }

  private void selectDay(int desiredDay) {
    ElementsCollection rows = dateRows.snapshot();

    for (SelenideElement row : rows) {
      ElementsCollection days = row.$$("button").snapshot();
      for (SelenideElement day : days) {
        if (day.getText().equals(String.valueOf(desiredDay))) {
          day.click();
          return;
        }
      }
    }
  }

  private String getMonthNameByIndex(int mothIndex) {
    return Month.of(mothIndex + 1).name();
  }

  private int getActualMonthIndex() {
    return Month.valueOf(currentMonthAndYear.getText()
            .split(" ")[0]
            .toUpperCase())
        .ordinal();
  }

  private int getActualYear() {
    return Integer.parseInt(currentMonthAndYear.getText().split(" ")[1]);
  }
}
