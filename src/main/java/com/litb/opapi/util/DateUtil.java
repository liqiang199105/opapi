package com.litb.opapi.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class DateUtil {
    public static String DATETIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static String getSimpleStringDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT_PATTERN);
		return sdf.format(date);
	}

	/**
	 * 估计时间获取是星期几，星期一返回1，星期日返回0
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.SUNDAY);
		c.setTime(date);
		return c.get(Calendar.DAY_OF_WEEK)-1;
	}

	public static String getStringDate(Date date, String pattern) {		
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.format(date);
	}

	public static Date getBeginOfDay(Date date) {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	public static Date getEndOfDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}

    public static Date parse(String str) {
        return parse(str, DATETIME_FORMAT_PATTERN);
    }
    public static Date parse(String str, String pattern) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        DateFormat parser = new SimpleDateFormat(pattern);
        try {
            return parser.parse(str);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Can't parse " + str + " using " + pattern);
        }
    }
    
	/**
	 * 根据day提供的日期，和time提供的时分秒，拼接出一个date时间
	 * @param day
	 * @param time
	 * @return
	 */
	public static Date getContactDate(Date day, Date time) {
		String dayStr = getStringDate(day, "yyyy-MM-dd");
		String timeStr = getStringDate(time, "HH:mm:ss");
		Date date = parse(dayStr + " " + timeStr);
		return date;
	}

	public static int getWeekOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		return c.get(Calendar.WEEK_OF_MONTH);
	}
	
	public static void main(String[] args) {
		Date date = DateUtil.parse("2015-02-01 11:20:10");
		System.out.println(DateUtil.getContactDate(new Date(), date));
		System.out.println(DateUtil.getDayOfWeek(new Date()));
		System.out.println(DateUtil.getWeekOfMonth(date));
	}
	
}
