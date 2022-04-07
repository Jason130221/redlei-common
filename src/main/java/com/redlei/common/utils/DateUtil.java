package com.redlei.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

	private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmssS");

	private static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMMdd");

	private static SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMddHHmmss");

	private static SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy-MM-dd");

	private static SimpleDateFormat sdf6 = new SimpleDateFormat("yyyy-M-d");

	private static SimpleDateFormat sdf7 = new SimpleDateFormat("yyyy年MM月dd日");

	private static SimpleDateFormat sdf8 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	private static SimpleDateFormat sdf9 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	private static String dateStr = "yyyy-MM-dd";

	public static String getCurrentDate() {

		// noinspection AlibabaAvoidCallStaticSimpleDateFormat
		return sdf3.format(new Date());
	}

	public static Date parseDateNewFormat(String sDate) {
		Date d = null;
		if (sDate != null) {
			if (sDate.length() == "yyyy-MM-dd HH:mm:ss".length()) {
				try {
					d = sdf9.parse(sDate);
				} catch (ParseException ex) {
					return null;
				}
			} else {
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:m:s");
					d = dateFormat.parse(sDate);
				} catch (ParseException ex) {
					return null;
				}
			}
		}
		return d;
	}

	synchronized public static String getLongCurrentDate() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}

		return sdf2.format(new Date());
	}

	public static String getShortCurrentDate() {

		return sdf3.format(new Date());
	}

	public static String getShortTomorrowDate() {
		long dateTime = new Date().getTime();
		Date date = new Date(dateTime + (24 * 60 * 60 * 1000));
		return sdf3.format(date);
	}

	public static Date praseStringDate(String dateStr) {
		try {
			Date date = sdf1.parse(dateStr);
			return date;
		} catch (ParseException e) {
//			e.printStackTrace();
		}
		return null;
	}

	public static Date praseShortStringDate(String dateStr) {
		try {
			Date date = sdf5.parse(dateStr);
			return date;
		} catch (ParseException e) {
//			e.printStackTrace();
		}
		return null;
	}

	public static Long parseDateStringToShortZeor(Object dateString) {
		if (dateString == null) {
			return null;
		}
		String temp = dateString.toString();
		if (StringUtil.isEmpty(temp)) {
			return null;
		}
		if (StringUtil.isDouble(temp)) {
			return Long.parseLong(temp);
		}
		Date dt = DateUtil.praseStringDate(temp);
		if (dt != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dt);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);

			return calendar.getTimeInMillis();
		}
		return null;
	}

	public static String getCurrentDate2() {
		return sdf4.format(new Date());
	}

	public static String getCurrentDate5() {
		return sdf5.format(new Date());
	}

	public static String formatDateStr(String dateStr) {
		Date date;
		String temp = dateStr.toString();
		if (StringUtil.isEmpty(temp)) {
			return null;
		}
		if (StringUtil.isDouble(temp)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(Long.parseLong(dateStr));
			date = calendar.getTime();
		} else {
			date = DateUtil.praseStringDate(temp);
		}
		return sdf3.format(date);
	}

	public static String formatDate2Str(String str) {
		Date date;
		try {
			date = new SimpleDateFormat(dateStr).parse(str);
			return sdf5.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String formatDate(Date date, String format) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

		return simpleDateFormat.format(date);
	}

	public static String cutOutDate(String date) {
		return date.substring(0, 10);
	}

	public static int getDaysBetweenTwoDate(Date beginDate, Date endDate) {
		int days = 0;
		long times = endDate.getTime() - beginDate.getTime();
		days = (int) (times / 86400000);
		return days;
	}

	public static boolean isSflb(String endInvestDate) {
		boolean flag = false;
		try {
			Date date = sdf1.parse(endInvestDate);
			long times = date.getTime() - new Date().getTime();
			if (times < 43200000) {
				flag = true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public static String getYesterday() {
		Date date = new Date(new Date().getTime() - 86400000);

		return sdf3.format(date);
	}

	public static String getYesterday2() {
		Date date = new Date(new Date().getTime() - 86400000);

		return sdf5.format(date);
	}

	public static String formartStringtoDate(String date) {

		try {
			Date formatDate = sdf3.parse(date);
			return sdf5.format(formatDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean dateGreaterOneDay(String date) {
		boolean flag = false;
		try {
			Date formatDate = sdf1.parse(date);
			long times = formatDate.getTime() - new Date().getTime();
			if (times < 86400000) {
				flag = true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public static boolean dateGreaterOneDay2(String date) {
		boolean flag = true;
		try {
			Date formatDate = sdf1.parse(date);
			long times = new Date().getTime() - formatDate.getTime();
			if (times > 86400000) {
				flag = false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public static int differentDays(Date beginDate, Date endDate) {
		Calendar beforeCal = Calendar.getInstance();
		beforeCal.setTime(beginDate);
		Calendar afterCal = Calendar.getInstance();
		afterCal.setTime(endDate);
		int beforeYear = beforeCal.get(Calendar.YEAR);
		int afterYear = afterCal.get(Calendar.YEAR);
		int days = 0;
		if (beforeYear == afterYear) {
			int beforeDay = beforeCal.get(Calendar.DAY_OF_YEAR);
			int afterDay = afterCal.get(Calendar.DAY_OF_YEAR);
			days = Math.abs(afterDay - beforeDay);
		} else {
			while (beforeCal.before(afterCal)) {
				days++;
				beforeCal.add(Calendar.DAY_OF_YEAR, 1);
			}
		}
		return days;
	}

	public static int differentDays(String beginDateStr, String endDateStr) {

		return differentDays(praseStringDate(beginDateStr), praseStringDate(endDateStr));
	}

	/**
	 * 给出某月的1号0点时间
	 *
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getMonthStartTime(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, (month - 1));
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date time = calendar.getTime();
		return sdf1.format(time);
	}

	/**
	 * 给出某月的最后一天23：59：59
	 *
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getMonthEndTime(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, (month - 1));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		Date time = calendar.getTime();
		return sdf1.format(time);
	}

	/**
	 * 给出某月的1号0点时间
	 *
	 * @param year
	 * @param month
	 * @return
	 */
	public static Long getMillisMonthStartTime(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, (month - 1));
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTimeInMillis();
	}

	/**
	 * 给出某月的最后一天23：59：59
	 *
	 * @param year
	 * @param month
	 * @return
	 */
	public static Long getMillisMonthEndTime(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, (month - 1));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTimeInMillis();
	}

	public static String formatsDate(Date date) {
		try {
			return sdf8.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String formatDate(Date date) {
		try {
			return sdf1.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<String> getAllDayDateList(String startTime, String endTime) {
		List<String> resultList = new ArrayList<String>();
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(DateUtil.praseStringDate(endTime));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtil.praseStringDate(startTime));
		while (calendar.before(endCalendar)) {
			resultList.add(sdf6.format(calendar.getTime()));
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		return resultList;
	}

	public static String getAddDay(Date date, Long day, String format) {
		long endTime = date.getTime() + day * 24 * 60 * 60 * 1000;
		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		String time = dateformat.format(endTime);
		return time;
	}

	public static String getLongToDateDay(Long day, String format) {
		if (day == null) {
			return "";
		}
		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		String time = dateformat.format(day);
		return time;
	}

	/**
	 * 查询上个月份
	 *
	 * @return
	 * @throws ParseException
	 */
	public static String getLastMonth() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -1);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon.substring(0, 7);
	}

	/**
	 * 获取昨天0点的时间戳
	 *
	 * @return
	 */
	public static long getYesterDay() {
		long now = System.currentTimeMillis();
		long time = now - 86400000;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String yesterDay = sdf.format(new Date(time)) + " 00:00:00";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long yesterDayTime = 0L;
		try {
			Date date = simpleDateFormat.parse(yesterDay);
			yesterDayTime = date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return yesterDayTime;
	}

	public static long getYesterDay(Date date) {
		long time = date.getTime() - 86400000;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String yesterDay = sdf.format(new Date(time));
		long yesterDayTime = 0L;
		try {
			Date dt = sdf.parse(yesterDay);
			yesterDayTime = dt.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return yesterDayTime;
	}

	/**
	 * 获取本周的第一天
	 *
	 * @return String
	 **/
	public static String getWeekStart() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.WEEK_OF_MONTH, 0);
		cal.set(Calendar.DAY_OF_WEEK, 2);
		Date time = cal.getTime();
		Date date = new Date();
		long timeL = time.getTime();
		long dateL = date.getTime();
		if (dateL < timeL) {
			cal.add(Calendar.WEEK_OF_MONTH, -1);
			cal.set(Calendar.DAY_OF_WEEK, 2);
			time = cal.getTime();
		}
		return new SimpleDateFormat("yyyy-MM-dd").format(time) + " 00:00:00";
	}

	/**
	 * @Author bjb
	 * @Description //获取某刻时间的季度
	 * @Date 2019/9/16 11:03
	 */
	public static Long getSeason(Long date) {
		Long season = 0L;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);
		int month = c.get(Calendar.MONTH);
		switch (month) {
		case Calendar.JANUARY:
		case Calendar.FEBRUARY:
		case Calendar.MARCH:
			season = 1L;
			break;
		case Calendar.APRIL:
		case Calendar.MAY:
		case Calendar.JUNE:
			season = 2L;
			break;
		case Calendar.JULY:
		case Calendar.AUGUST:
		case Calendar.SEPTEMBER:
			season = 3L;
			break;
		case Calendar.OCTOBER:
		case Calendar.NOVEMBER:
		case Calendar.DECEMBER:
			season = 4L;
			break;
		default:
			break;
		}
		return season;
	}

	// 装成年月日
	public static String stampToTime(Long s) {
		String res = null;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Date date = new Date(s);
			res = simpleDateFormat.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	// 装成年月日
	public static String stampTo2Time(Long s) {
		String res = null;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date(s);
			res = simpleDateFormat.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	// 装成年月
	public static String stampTo3Time(Long s) {
		String res = null;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
			Date date = new Date(s);
			res = simpleDateFormat.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	// 装成年月日
	public static String longToTime(Long s) {
		String res = null;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date(s);
			res = simpleDateFormat.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	// 装成年月日
	public static String stringToTime(String s) {
		String res = null;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date(Long.parseLong(s));
			res = simpleDateFormat.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public static Long pareseShortDateStrToLong(String date) {
		if (StringUtil.isDouble(date)) {
			int len = date.length();
			if (len > 13 || len < 10) {
				// 错误时间
				return null;
			}

			if (date.length() < 13) {
				while (len < 13) {
					date += "0";
					len++;
				}
				return Long.parseLong(date);
			}
			return Long.parseLong(date);
		}
		Date dt = DateUtil.praseShortStringDate(date);
		if (dt != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dt);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);

			return calendar.getTimeInMillis();
		}
		return null;
	}

	public static String formatLongToLongDate(Long longTime) {
		if (longTime == null) {
			return "";
		}
		Date date = new Date(longTime);
		return sdf1.format(date);
	}

	/**
	 * 生成随机文件名：当前年月日时分秒+五位随机数  
	 *
	 * @return
	 */
	public static String getRandom() {
		String str = new SimpleDateFormat("yyyyMMdd").format(new Date());
		int rannum = (int) (new Random().nextDouble() * (999999999 - 100000000 + 1)) + 100000000;// 获取9位随机数
		return str + rannum;
	}

	/**
	 * 根据年 月 获取对应的月份 天数  
	 */
	public static int getDaysByYearMonth(int year, int month) {

		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	public static String getAfterMonth(String inputDate, int number) {
		Calendar c = Calendar.getInstance();// 获得一个日历的实例
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(inputDate);// 初始日期
		} catch (Exception e) {

		}
		c.setTime(date);// 设置日历时间
		c.add(Calendar.MONTH, number);// 在日历的月份上增加6个月
		String strDate = sdf.format(c.getTime());// 的到你想要得6个月后的日期
		return strDate;
	}

	/**
	 * 获取年月日格式日期
	 *
	 * @return
	 */
	public static String getFormatDate() {
		return sdf7.format(new Date());
	}

	public static Integer getDaysOfMonth(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = new GregorianCalendar();
		Date date1 = null;
		try {
			date1 = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(date1); // 放入你的日期
		int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		System.out.println("天数为=" + actualMaximum);
		return actualMaximum;
	}

	/**
	 * 获取格式为yyyy-MM-dd的时间
	 *
	 * @param timestamp
	 * @return
	 */
	public static String getFormat7String(Long timestamp) {
		Date date = new Date(timestamp);
		return sdf7.format(date);
	}

	/**
	 * 时间戳+天数
	 */
	public static Long getAfterDay(Long time, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + day);
		System.out.println(calendar.getTimeInMillis()); // 1541840067116
		return calendar.getTimeInMillis();
	}

	/**
	 * 增加月
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public static Date addMonths(Date date, int amount) {
		return add(date, Calendar.MONTH, amount);
	}

	public static Date add(Date date, int calendarField, int amount) {
		if (date == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(calendarField, amount);
		return c.getTime();
	}
}