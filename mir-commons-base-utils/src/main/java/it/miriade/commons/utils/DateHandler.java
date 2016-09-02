package it.miriade.commons.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author svaponi
 * @created Mar 16, 2015 12:04:41 PM 2015-03-01
 */
public class DateHandler {

	public enum TimeInterval {
		MILLISECOND(1), SECOND(1000), MINUTE(1000 * 60), HOUR(1000 * 60 * 60), DAY(1000 * 60 * 60 * 24), WEEK(1000 * 60 * 60 * 24 * 7), MONTH(
				1000 * 60 * 60 * 24 * 30), YEAR(1000 * 60 * 60 * 24 * 365), YEAR_LEAP(1000 * 60 * 60 * 24 * 366);

		private long time;

		private TimeInterval(long time) {
			this.time = time;
		}

		public long ms() {
			return this.time;
		}

		public long s() {
			return this.time / SECOND.time;
		}

		public long m() {
			return this.time / MINUTE.time;
		}

		public long h() {
			return this.time / HOUR.time;
		}

		public long days() {
			return this.time / DAY.time;
		}

	}

	/**
	 * Impedisce che venga tritornato NULL in caso di errore.
	 */
	public static boolean AVOID_NULL = false;

	// YEARs

	public static boolean isBeforeYears(int years, Date aDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, years);
		return aDate.compareTo(calendar.getTime()) < 0;
	}

	public static Date getYearsAgo(int years) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -years);
		return cal.getTime();
	}

	// MONTHs

	public static boolean isBeforeMonths(int months, Date aDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, months);
		return aDate.compareTo(calendar.getTime()) < 0;
	}

	public static Date getMonthsAgo(int months) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -months);
		return cal.getTime();
	}

	public static Date addMonths(Date fromDate, int deltaMonths) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fromDate);
		cal.add(Calendar.MONTH, deltaMonths);
		return cal.getTime();
	}

	// WEEKs

	public static boolean isBeforeWeeks(int weeks, Date aDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.WEEK_OF_YEAR, weeks);
		return aDate.compareTo(calendar.getTime()) < 0;
	}

	public static Date getWeeksAgo(int weeks) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.WEEK_OF_YEAR, -weeks);
		return cal.getTime();
	}

	public static Date addWeeks(Date fromDate, int deltaWeeks) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fromDate);
		cal.add(Calendar.WEEK_OF_YEAR, deltaWeeks);
		return cal.getTime();
	}

	// DAYs

	public static boolean isBeforeDays(int days, Date aDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, days);
		return aDate.compareTo(calendar.getTime()) < 0;
	}

	public static Date getDaysAgo(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -days);
		return cal.getTime();
	}

	public static Date addDays(Date fromDate, int deltaDays) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fromDate);
		cal.add(Calendar.DAY_OF_YEAR, deltaDays);
		return cal.getTime();
	}

	// SECONDS

	public static boolean isBeforeSeconds(int seconds, Date aDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, seconds);
		return aDate.compareTo(calendar.getTime()) < 0;
	}

	public static Date getSecondsAgo(int seconds) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, -seconds);
		return cal.getTime();
	}

	public static Date addSeconds(Date fromDate, int deltaSeconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fromDate);
		cal.add(Calendar.SECOND, deltaSeconds);
		return cal.getTime();
	}

	// GENERIC

	public static boolean isBefore(int time, Date aDate, int unit) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(unit, time);
		return aDate.compareTo(calendar.getTime()) < 0;
	}

	public static Date getAgo(int time, int unit) {
		Calendar cal = Calendar.getInstance();
		cal.add(unit, -time);
		return cal.getTime();
	}

	public static Date add(Date fromDate, int delta, int unit) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fromDate);
		cal.add(unit, delta);
		return cal.getTime();
	}

	public enum Format {
		/**
		 * dd-MM-yyyy
		 */
		ITALIAN_DATE("dd-MM-yyyy"),
		/**
		 * MM-dd-yyyy
		 */
		ENGLISH_DATE("MM-dd-yyyy"),

		/**
		 * yyyy-MM-dd
		 */
		DATE("yyyy-MM-dd"),
		/**
		 * HH:mm:ss
		 */
		TIME("HH:mm:ss"),
		/**
		 * HH:mm:ss.SSS
		 */
		INSTANT("HH:mm:ss.SSS"),
		/**
		 * yyyy-MM-dd HH:mm:ss.SSS
		 */
		TIMESTAMP("yyyy-MM-dd HH:mm:ss.SSS"),

		/**
		 * yyyyMMdd
		 */
		CONDENSED_DATE("yyyyMMdd"),
		/**
		 * HHmmss
		 */
		CONDENSED_TIME("HHmmss"),
		/**
		 * HHmmssSSS
		 */
		CONDENSED_INSTANT("HHmmssSSS"),
		/**
		 * yyyyMMddHHmmssSSS
		 */
		CONDENSED_TIMESTAMP("yyyyMMddHHmmssSSS"),

		/**
		 * yyyy-MM-dd-HH-mm-ss-SSS<br>
		 * Fatto apposta per essere splittato con "-", esempio:
		 * 
		 * <pre>
		 * String[] arr = DateHandler.currentDate(Format.EASY_DECODE_TIMESTAMP).split("-");
		 * String year = arr[0];
		 * String month = arr[1];
		 * String day = arr[2];
		 * ...
		 * String millis = arr[6];
		 * </pre>
		 */
		EASY_DECODE_TIMESTAMP("yyyy-MM-dd-HH-mm-ss-SSS"),
		/**
		 * yyyy-MM-dd'T'HH:mm'Z
		 */
		ISO_DATE("yyyy-MM-dd'T'HH:mm'Z'");

		private final String pattern;

		private Format(final String pattern) {
			this.pattern = pattern;
		}

		@Override
		public String toString() {
			return pattern;
		}

		public String toPattern() {
			return pattern;
		}
	}

	/**
	 * Genera un {@link SimpleDateFormat}.
	 * 
	 * @param format
	 * @return
	 */
	public static SimpleDateFormat getDateFormatter(Format format) {
		return new SimpleDateFormat(format.pattern);
	}

	/**
	 * Genera un {@link SimpleDateFormat}.
	 * 
	 * @param format
	 * @return
	 */
	public static SimpleDateFormat getDateFormatter(String dateFormat) {
		return new SimpleDateFormat(dateFormat);
	}

	/*
	 * Format Current Date/time
	 */

	/**
	 * Ritorna la string della data corrente usando il pattern in input.
	 * 
	 * @param pattern
	 *            pattern per la formattazione della data corrente
	 * @return
	 */
	public static String current(String pattern) {
		return new SimpleDateFormat(pattern).format(new Date());
	}

	/**
	 * Ritorna la string della data corrente usando il formato in input.
	 * 
	 * @param format
	 *            formato della data corrente
	 * @return
	 */
	public static String current(Format format) {
		return current(format.pattern);
	}

	/**
	 * Ritorna la string della data corrente in formato {@link Format.DATE}.
	 * 
	 * @return
	 */
	public static String currentDate() {
		return current(Format.DATE);
	}

	/**
	 * Ritorna la string del timestamp corrente in formato {@link Format.TIMESTAMP}.
	 * 
	 * @return
	 */
	public static String currentTimestamp() {
		return current(Format.TIMESTAMP);
	}

	/*
	 * Format
	 */

	/**
	 * Stringa tornata quando si tenta di formattare una Data null
	 */
	public static final String NULL_DATE_TOSTRING = "";

	public static String formatDate(Date date, String pattern) {
		if (date == null)
			return NULL_DATE_TOSTRING;
		return new SimpleDateFormat(StringHandler.toString(pattern)).format(date);
	}

	public static String formatDate(Date date, Format format) {
		return formatDate(date, format.pattern);
	}

	/*
	 * Format timestamp
	 */
	public static String formatTimestamp(long timestamp, String pattern) {
		Date date = new Date(timestamp);
		return new SimpleDateFormat(pattern).format(date);
	}

	public static String formatTimestamp(long timestamp, Format format) {
		return formatTimestamp(timestamp, format.pattern);
	}

	public static String formatTimestamp(Timestamp timestamp, String pattern) {
		return new SimpleDateFormat(pattern).format(timestamp);
	}

	public static String formatTimestamp(Timestamp timestamp, Format format) {
		return formatTimestamp(timestamp, format.pattern);
	}

	/*
	 * Parse String to timestamp
	 */
	public static Timestamp parseTimestamp(String dateStr, String pattern) {
		try {
			return new Timestamp(((Date) new SimpleDateFormat(pattern).parseObject(dateStr)).getTime());
		} catch (Exception e) {
		}
		if (!AVOID_NULL)
			return null;
		Date date = new Date();
		return new Timestamp(date.getTime());
	}

	public static Timestamp parseTimestamp(String dateStr, Format format) {
		try {
			return new Timestamp(((Date) getDateFormatter(format).parseObject(dateStr)).getTime());
		} catch (Exception e) {
		}
		if (!AVOID_NULL)
			return null;
		Date date = new Date();
		return new Timestamp(date.getTime());
	}

	/*
	 * Parse String to Date
	 */
	public static Date parseDate(String dateStr, String pattern) {
		try {
			return (Date) new SimpleDateFormat(pattern).parseObject(dateStr);
		} catch (Exception e) {
		}
		if (!AVOID_NULL)
			return null;
		return new Date();
	}

	public static Date parseDate(String dateStr, Format format) {
		try {
			return (Date) getDateFormatter(format).parseObject(dateStr);
		} catch (Exception e) {
		}
		if (!AVOID_NULL)
			return null;
		return new Date();
	}

}
