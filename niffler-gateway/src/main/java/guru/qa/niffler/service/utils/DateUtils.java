package guru.qa.niffler.service.utils;

import guru.qa.niffler.model.DataFilterValues;
import jakarta.annotation.Nonnull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@ParametersAreNonnullByDefault
public class DateUtils {

  public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

  public static @Nonnull
  String dateFormat(Date date, String pattern) {
    return new SimpleDateFormat(pattern).format(date);
  }

  public static @Nonnull
  String dateFormat( Date date) {
    return dateFormat(date, DEFAULT_DATE_FORMAT);
  }

  public static @Nonnull
  Date filterDate(DataFilterValues filter) {
    Date currentDate = new Date();
    return switch (filter) {
      case TODAY -> currentDate;
      case WEEK -> addDaysToDate(currentDate, Calendar.WEEK_OF_MONTH, -1);
      case MONTH -> addDaysToDate(currentDate, Calendar.MONTH, -1);
    };
  }

  public static @Nonnull
  Date addDaysToDate(Date date, int selector, int days) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(selector, days);
    return cal.getTime();
  }
}
