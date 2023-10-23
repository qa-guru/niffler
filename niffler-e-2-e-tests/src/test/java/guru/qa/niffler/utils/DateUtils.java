package guru.qa.niffler.utils;

import javax.annotation.Nonnull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    @Nonnull
    public static String getDateAsString(@Nonnull Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy");
        return sdf.format(date);
    }

    @Nonnull
    public static String getDateAsString(@Nonnull Date date, @Nonnull String stringFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(stringFormat);
        return sdf.format(date);
    }

    @Nonnull
    public static Date fromString(@Nonnull String dateAsString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy");
        try {
            return sdf.parse(dateAsString);
        } catch (ParseException e) {
            throw new RuntimeException();
        }
    }

    @Nonnull
    public static Date addDaysToDate(@Nonnull Date date, int selector, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(selector, days);
        return cal.getTime();
    }
}
