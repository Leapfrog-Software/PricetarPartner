package jp.co.lfg.pricetarpartner.System;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtility {

    public static String dateToString(Date date, String format) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        return dateFormat.format(date);
    }

    public static Date stringToDate(String string, String format) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
            return dateFormat.parse(string);
        } catch (Exception e) {
            return null;
        }
    }

    public static Date addDay(Date date, int additional) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, additional);
        return calendar.getTime();
    }

    public static Date addSecond(Date date, int additional) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, additional);
        return calendar.getTime();
    }
}
