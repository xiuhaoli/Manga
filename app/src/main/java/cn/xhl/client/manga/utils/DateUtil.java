package cn.xhl.client.manga.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Mike on 2017/10/25 0025.
 */

public class DateUtil {

    private static Calendar dateFormat(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        Calendar calendar;
        try {
            Date date = sf.parse(String.valueOf(time));
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return calendar;
    }

    public static String stampToDateWithHMS(long s) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(s * 1000);
        String minute;
        if (calendar.get(Calendar.MINUTE) < 10) {
            minute = "0" + calendar.get(Calendar.MINUTE);
        } else {
            minute = String.valueOf(calendar.get(Calendar.MINUTE));
        }
        String second;
        if (calendar.get(Calendar.SECOND) < 10) {
            second = "0" + calendar.get(Calendar.SECOND);
        } else {
            second = String.valueOf(calendar.get(Calendar.SECOND));
        }
        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1)
                + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " "
                + calendar.get(Calendar.HOUR_OF_DAY) + ":" + minute + ":" + second;
    }

    /**
     * 将时间戳转换成时间
     *
     * @param s 十位数字
     * @return
     */
    public static String stampToDate(long s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date(s * 1000);
        return simpleDateFormat.format(date);
    }

    public static String stampToMonthAndDay(long s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd", Locale.getDefault());
        Date date = new Date(s * 1000);
        return simpleDateFormat.format(date);
    }

    public static String stampToHourAndMinute(long s) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(s * 1000);
        String minute;
        if (calendar.get(Calendar.MINUTE) < 10) {
            minute = "0" + calendar.get(Calendar.MINUTE);
        } else {
            minute = String.valueOf(calendar.get(Calendar.MINUTE));
        }
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + minute;
    }

    public static boolean isToday(long s) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(s * 1000);
        Calendar today = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isThisYear(long s) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(s * 1000);
        Calendar today = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR);
    }

}
