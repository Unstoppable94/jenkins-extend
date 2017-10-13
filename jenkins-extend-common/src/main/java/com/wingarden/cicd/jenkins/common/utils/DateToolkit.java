
package com.wingarden.cicd.jenkins.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DateToolkit {

    static final String[] NORMAL_DATEFORMATS = new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd",
            "yyyy-MM-dd HH:mm:ss.SSS", "yyyyMMdd", "yyyyMMddHHmmss", "yyyy-MM-dd a", "HH:mm:ss" };

    public static final String DEFAULT_DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE = "yyyy-MM-dd";

    public static String normalDateFormat(String dateStr) {
        for (String pattern : NORMAL_DATEFORMATS) {
            if (isValidDateFormat(dateStr, pattern)) {
                return pattern;
            }
        }
        return null;
    }

    public static Date normalDate(String dateStr) {
        String dateFormat = normalDateFormat(dateStr);
        return null != dateFormat ? utilStrToDate(dateStr, dateFormat) : null;
    }

    public static String sqlDateToStr(java.sql.Date date) {
        return formatSqlDate(date, "yyyy-MM-dd");
    }

    public static String formatSqlDate(java.sql.Date date, String pattern) {
        if (date == null)
            return "";
        DateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static java.sql.Date strToSqlDate(String datetime) {
        return strToSqlDate(datetime, "yyyy-MM-dd");
    }

    public static java.sql.Date strToSqlDate(String datetime, String pattern) {
        java.sql.Date result = null;
        try {
            if ((datetime != null) && (datetime.length() > 0)) {
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                result = new java.sql.Date(format.parse(datetime).getTime());
            }
        } catch (Exception exp) {
            throw new RuntimeException("have a NG date format");
        }
        return result;
    }

    public static String utilDateToStr(java.util.Date date) {
        return formatUtilDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatUtilDate(java.util.Date date, String pattern) {
        if (date == null)
            return "";
        DateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static String formatUtilDate(java.util.Date date, String pattern, Locale locale) {
        if (date == null)
            return "";
        DateFormat format = new SimpleDateFormat(pattern, locale);
        return format.format(date);
    }

    public static java.util.Date utilStrToDate(String datetime) {
        return utilStrToDate(datetime, "yyyy-MM-dd HH:mm:ss");
    }

    public static java.util.Date utilStrToDate(String datetime, String pattern) {
        java.util.Date result;
        try {
            if ((datetime != null) && (datetime.length() > 0)) {
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                result = format.parse(datetime);
            } else {
                result = null;
            }
        } catch (Exception exp) {
            // 指定的日期字符串格式不对
            throw new RuntimeException("have a NG date format");
        }
        return result;
    }

    public static int compareMonth(java.util.Date d1, java.util.Date d2) {
        java.util.Calendar c1 = java.util.Calendar.getInstance();
        c1.setTime(d1);
        java.util.Calendar c2 = java.util.Calendar.getInstance();
        c2.setTime(d2);

        return (c1.get(java.util.Calendar.YEAR) - c2
                .get(java.util.Calendar.YEAR))
                * 12
                + (c1.get(java.util.Calendar.MONTH) - c2
                        .get(java.util.Calendar.MONTH));
    }

    public static int compareSecond(java.util.Date d1, java.util.Date d2) {

        return (int) ((d2.getTime() - d1.getTime()) / 1000);
    }

    public static int compareHour(java.util.Date d1, java.util.Date d2) {

        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60));
    }

    public static int compareMonth(java.sql.Date d1, java.sql.Date d2) {
        java.util.Calendar c1 = java.util.Calendar.getInstance();
        c1.setTime(d1);
        java.util.Calendar c2 = java.util.Calendar.getInstance();
        c2.setTime(d2);

        return (c1.get(java.util.Calendar.YEAR) - c2
                .get(java.util.Calendar.YEAR))
                * 12
                + (c1.get(java.util.Calendar.MONTH) - c2
                        .get(java.util.Calendar.MONTH));
    }

    public static String getMonthBegin(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return formatUtilDate(calendar.getTime(), "yyyy-MM-dd") + " 00:00:00";
    }

    public static String getMonthEnd(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 0);
        return formatUtilDate(calendar.getTime(), "yyyy-MM-dd") + " 23:59:59";
    }

    public static String yesterday(String pattern) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 1);
        return formatUtilDate(c.getTime(), pattern);
    }

    public static String preMonth(String pattern) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -1);
        return new SimpleDateFormat(pattern).format(c
                .getTime());
    }

    public static boolean isVaidYYMMDD(String dateString) {
        if (dateString == null)
            return false;
        return dateString.matches("^[1-9][0-9]{3}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$");
    }

    public static boolean isVaidYYMMDDHH(String dateString) {
        if (dateString == null)
            return false;
        return dateString.matches("^[1-9][0-9]{3}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])([0-1][0-9]|2[0-3])$");
    }

    public static boolean isValidDateFormat(String dateStr, String exepctedFormat) {
        try {
            utilStrToDate(dateStr, exepctedFormat);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }


    public static int getCurrentMonthDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public static String getDayOfWeekByDate(String date) {
        String dayOfweek = "-1";
        try {
            SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date myDate = myFormatter.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("E");
            String str = formatter.format(myDate);
            dayOfweek = str;

        } catch (Exception e) {
            System.out.println("错误!");
        }
        return dayOfweek;
    }

    public static Long minusTime(Date d1, Date d2) {
        return d1.getTime() - d2.getTime();
    }

    public static List<String> getDayList(String startdate, String enddate) {
        List<String> dateList = new ArrayList<String>();
        try {

            DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
            Calendar startDay = Calendar.getInstance();
            Calendar endDay = Calendar.getInstance();
            startDay.setTime(FORMATTER.parse(startdate));
            endDay.setTime(FORMATTER.parse(enddate));

            Calendar currentPrintDay = startDay;

            while (true) {
                if (currentPrintDay.after(endDay)) {
                    break;
                }
                dateList.add(FORMATTER.format(currentPrintDay.getTime()));

                // 日期加一
                currentPrintDay.add(Calendar.DATE, 1);

            }
        } catch (Exception e) {
            System.out.println("调用DateToolkit类的getCycleDayList方法报错！");
        }

        return dateList;
    }

    public static Integer transferTimeToMinutes(String time) {

        String[] array = time.split(":");
        if (array.length == 2) {
            return Integer.parseInt(array[0]) * 60 + Integer.parseInt(array[1]);
        } else
            return 0;
    }

    public static int getDateOfWeekInYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    public static String getSubDate(Date date, int n, String pattern) {
        Date getDate = new Date(date.getTime() + 86400000L * n);
        return formatUtilDate(getDate, pattern);
    }

    public static Date getSubDate(Date date, int n) {
        Date getDate = new Date(date.getTime() + 86400000L * n);
        return getDate;
    }
}
