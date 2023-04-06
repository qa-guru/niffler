package niffler.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String getDateAsString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy");
        return sdf.format(date);
    }

    public static String getDateAsString(Date date, String stringFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(stringFormat);
        return sdf.format(date);
    }

    public static Date fromString(String dateAsString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy");
        try {
            return sdf.parse(dateAsString);
        } catch (ParseException e) {
            throw new RuntimeException();
        }
    }

    public static Date addDaysToDate(Date date, int selector, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(selector, days);
        return cal.getTime();
    }
}
