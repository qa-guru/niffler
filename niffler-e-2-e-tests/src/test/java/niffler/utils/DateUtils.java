package niffler.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String getDateAsString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy");
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
}
