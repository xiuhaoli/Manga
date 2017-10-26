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
    /**
     * 获取日期
     *
     * @param time
     * @return
     */
    public static String getDate(String time) {
        Calendar calendar = dateFormat(time);
        if (calendar == null) return null;
        Calendar sysCalendar = Calendar.getInstance();
        if (sysCalendar.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH) < 2
                && sysCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && sysCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
            String hour = calendar.get(Calendar.HOUR_OF_DAY) + "";
            String minute = calendar.get(Calendar.MINUTE) + "";
            if (hour.equals("0")) hour = "12";
            if (hour.length() == 1) hour = "0" + hour;
            if (minute.length() == 1) minute = "0" + minute;
            return hour + ":" + minute;
        }
        String month = calendar.get(Calendar.MONTH) + 1 + "";
        String day = calendar.get(Calendar.DAY_OF_MONTH) + "";
        if (month.length() == 1) month = "0" + month;
        if (day.length() == 1) day = "0" + day;
        return month + "-" + day;
    }

    public static String getMonth(String time) {
        String[] month = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", "本月"};
        Calendar sysCalendar = Calendar.getInstance();
        Calendar calendar = dateFormat(time);
        if (calendar == null) {
            return month[12];
        }
        if (sysCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && sysCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
            return month[12];
        }
        int monthPos = calendar.get(Calendar.MONTH);
        return month[monthPos];
    }

    /**
     * 获取星期
     *
     * @param time
     * @return
     */
    public static String getWeek(String time) {
        String[] week = {"周一", "周二", "周三", "周四", "周五", "周六", "周日", "今天", "昨天"};
        Calendar calendar = dateFormat(time);
        Calendar sysCalendar = Calendar.getInstance();
        if (calendar == null) return null;
        int weekPos = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (sysCalendar.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH) == 0
                && sysCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && sysCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
            return week[7];
        } else if (sysCalendar.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH) == 1
                && sysCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && sysCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
            return week[8];
        }
        return week[weekPos];
    }

    private static Calendar dateFormat(String time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        Calendar calendar;
        try {
            Date date = sf.parse(time);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return calendar;
    }

    /**
     * 获取当前时间
     *
     * @return format yyyy-MM-dd hh:mm:ss
     */
    public static String getDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
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
}
