/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobifone.loaphuong.util;

import java.io.Serializable;
import java.text.*;
import java.util.*;

public class DateUtil implements Serializable {

    ////////////////////////////////////////////////////////
    /**
     * Check string format with current format locale
     *
     * @param strSource String to check
     * @return boolean true if strSource represent a date, otherwise false
     */
    ////////////////////////////////////////////////////////
    public static boolean isDate(String strSource) {
        return isDate(strSource, DateFormat.getDateInstance());
    }

    ////////////////////////////////////////////////////////
    /**
     * Check string format
     *
     * @param strSource String
     * @param strFormat Format to check
     * @return boolean true if strSource represent a date, otherwise false
     */
    ////////////////////////////////////////////////////////
    public static boolean isDate(String strSource, String strFormat) {
        SimpleDateFormat fmt = new SimpleDateFormat(strFormat);
        fmt.setLenient(false);
        return isDate(strSource, fmt);
    }

    ////////////////////////////////////////////////////////
    /**
     * Check string format
     *
     * @param strSource String
     * @param fmt Format to check
     * @return boolean true if strSource represent a date, otherwise false
     */
    ////////////////////////////////////////////////////////
    public static boolean isDate(String strSource, DateFormat fmt) {
        try {
            fmt.setLenient(false);
            if (fmt.parse(strSource) == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    ////////////////////////////////////////////////////////
    /**
     * Convert string to date using current format locale
     *
     * @param strSource String to convert
     * @return Date converted, null if conversion failure
     */
    ////////////////////////////////////////////////////////
    public static Date toDate(String strSource) {
        return toDate(strSource, DateFormat.getDateInstance());
    }

    ////////////////////////////////////////////////////////
    /**
     * Convert string to date
     *
     * @param strSource String to convert
     * @param strFormat Format to convert
     * @return Date converted, null if conversion failure
     */
    ////////////////////////////////////////////////////////
    public static Date toDate(String strSource, String strFormat) {
        SimpleDateFormat fmt = new SimpleDateFormat(strFormat);
        return toDate(strSource, fmt);
    }

    ////////////////////////////////////////////////////////
    /**
     * Convert string to date
     *
     * @param strSource String to convert
     * @param fmt Format to convert
     * @return Date converted, null if conversion failure
     */
    ////////////////////////////////////////////////////////
    public static Date toDate(String strSource, DateFormat fmt) {
        try {
            fmt.setLenient(false);
            return fmt.parse(strSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    ////////////////////////////////////////////////////////
    /**
     * Add date value by second
     *
     * @param dt Date Date to add
     * @param iValue int value to add
     * @return Date after add
     */
    ////////////////////////////////////////////////////////
    public static Date addSecond(Date dt, int iValue) {
        return add(dt, iValue, Calendar.SECOND);
    }

    ////////////////////////////////////////////////////////
    /**
     * Add date value by minute
     *
     * @param dt Date Date to add
     * @param iValue int value to add
     * @return Date after add
     */
    ////////////////////////////////////////////////////////
    public static Date addMinute(Date dt, int iValue) {
        return add(dt, iValue, Calendar.MINUTE);
    }

    ////////////////////////////////////////////////////////
    /**
     * Add date value by hour
     *
     * @param dt Date Date to add
     * @param iValue int value to add
     * @return Date after add
     */
    ////////////////////////////////////////////////////////
    public static Date addHour(Date dt, int iValue) {
        return add(dt, iValue, Calendar.HOUR);
    }

    ////////////////////////////////////////////////////////
    /**
     * Add date value by day
     *
     * @param dt Date Date to add
     * @param iValue int value to add
     * @return Date after add
     */
    ////////////////////////////////////////////////////////
    public static Date addDay(Date dt, int iValue) {
        return add(dt, iValue, Calendar.DATE);
    }

    ////////////////////////////////////////////////////////
    /**
     * Add date value by month
     *
     * @param dt Date Date to add
     * @param iValue int value to add
     * @return Date after add
     */
    ////////////////////////////////////////////////////////
    public static Date addMonth(Date dt, int iValue) {
        return add(dt, iValue, Calendar.MONTH);
    }

    ////////////////////////////////////////////////////////
    /**
     * Add date value by year
     *
     * @param dt Date Date to add
     * @param iValue int value to add
     * @return Date after add
     */
    ////////////////////////////////////////////////////////
    public static Date addYear(Date dt, int iValue) {
        return add(dt, iValue, Calendar.YEAR);
    }

    ////////////////////////////////////////////////////////
    /**
     * Add date value
     *
     * @param dt Date Date to add
     * @param iValue int value to add
     * @param iType type of unit
     * @return Date after add
     */
    ////////////////////////////////////////////////////////
    public static Date add(Date dt, int iValue, int iType) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(dt);
        cld.add(iType, iValue);
        return cld.getTime();
    }
    
    //Convert java Date sang sql Timestamp
    public static java.sql.Timestamp getSqlTimestamp(java.util.Date date) {
        if (date == null) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(date.getTime()));
        c.set(Calendar.MILLISECOND, 0);

        return new java.sql.Timestamp(c.getTimeInMillis());
    }
    ////////////////////////////////////////////////////////////////////////

    //Convert java Date sang sql Timestamp
    public static java.sql.Timestamp convertTimestamp(java.util.Date date) {
        return getSqlTimestamp(date);
    }

    //Convert java Date sang sql Date
    public static java.sql.Date getSqlDate(java.util.Date date) {
        if (date == null) {
            return null;
        }
        return new java.sql.Date(date.getTime());
    }
    ////////////////////////////////////////////////////////////////////////

    //Convert java Date thành String
    public static String convertDateToString(java.util.Date date) {
        if (date == null) {
            return null;
        }
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }
    ////////////////////////////////////////////////////////////////////////

    //Convert java Date Time thành String
    public static String convertDateTimeToString(java.util.Date date) {
        if (date == null) {
            return null;
        }
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatter.format(date);
    }
    
    public static String dateToString(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String sDate = formatter.format(date);
		return sDate;
    }
    public static java.sql.Timestamp stringToDateTime(String sdate) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = (Date) formatter.parse(sdate);
		//String sd = dateToString2(date);
		return getSqlTimestamp(date);
	}
    
    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);
        return calendar.getTime();
    }
    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    }

    public static List<Integer> convertTimeToNumber(String timeString){
        List<Integer> numList = new ArrayList<>();
        timeString = timeString.replaceAll("\\s","");
        List<String> timeList = Arrays.asList(timeString.split(","));
  
        for (String string : timeList) {
            int hh = Integer.parseInt(Arrays.asList(string.split(":")).get(0));
            int mm = Integer.parseInt(Arrays.asList(string.split(":")).get(1));
            int ss = Integer.parseInt(Arrays.asList(string.split(":")).get(2));
            
            if (hh > 23 || mm > 59) {
                return new ArrayList<>();
            }
            int num =  hh * 3600 + mm * 60 + ss ;
            numList.add(num);
        }
        return numList;
    }
}
