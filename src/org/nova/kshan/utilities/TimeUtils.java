package org.nova.kshan.utilities;

import org.nova.utility.misc.Misc;

/**
 * 
 * @author K-Shan
 *
 */
public class TimeUtils {
	
	/**
	 * Returns the number of days in milliseconds
	 * @param days
	 * @return
	 */
	public static long getDays(int days) {
		return (((60 * 1000) * 60) * ((long) (days * 24)));
	}
	
	/**
	 * Returns the number of hours in milliseconds
	 * @param hours
	 * @return
	 */
	public static long getHours(int hours) {
		return (((60 * 1000) * 60) * (long) hours);
	}
	
	/**
	 * Returns the number of minutes in milliseconds
	 * @param hours
	 * @return
	 */
	public static long getMinutes(int minutes) {
		return (60 * 1000) * (long) minutes;
	}
	
	/**
	 * Returns the number of seconds in milliseconds
	 * @param hours
	 * @return
	 */
	public static long getSeconds(int seconds) {
		return ((long) seconds * 1000);
	}
	
	/**
	 * Converts time that is in milliseconds and greater than currentTimeMillis() into days.
	 * @param time
	 * @return
	 */
	public static int convertToDays(long time) {
		if(Misc.currentTimeMillis() > time || Misc.currentTimeMillis() == time)
			return -1;
		long remaining = ((long) (time - Misc.currentTimeMillis()));
		return (int) (remaining / (((60 * 1000) * 60) * 24));
	}

	/**
	 * Converts time that is in milliseconds and greater than currentTimeMillis() into hours.
	 * @param time
	 * @return
	 */
	public static int convertToHours(long time) {
		if(Misc.currentTimeMillis() > time || Misc.currentTimeMillis() == time)
			return -1;
		long remaining = ((long) (time - Misc.currentTimeMillis()));
		return (int) (remaining / (((60 * 1000) * 60)));
	}
	
	/**
	 * Converts time that is in milliseconds and greater than currentTimeMillis() into minutes.
	 * @param time
	 * @return
	 */
	public static int convertToMinutes(long time) {
		if(Misc.currentTimeMillis() > time || Misc.currentTimeMillis() == time)
			return -1;
		long remaining = ((long) (time - Misc.currentTimeMillis()));
		return (int) (remaining / (60 * 1000));
	}
	
	/**
	 * Converts time that is in milliseconds and greater than currentTimeMillis() into seconds.
	 * @param time
	 * @return
	 */
	public static int convertToSeconds(long time) {
		if(Misc.currentTimeMillis() > time || Misc.currentTimeMillis() == time)
			return -1;
		long remaining = ((long) (time - Misc.currentTimeMillis()));
		return (int) (remaining / (1000));
	}
	
	/**
	 * Returns a formatted string that includes days, hours, minutes and seconds according
	 * to what the @param time variable's value is.
	 * @param time
	 * @return
	 */
	public static String formatCountdown(long time, boolean showDays, 
			boolean showHours, boolean showMinutes, boolean showSeconds) {
		String append = "";
		long remaining = time;
		if(convertToDays(remaining) > 0 && showDays) {
			append = convertToDays(remaining) + " day"+(convertToDays(remaining) == 1 ? "" : "s")+" ";
			long temp = remaining;
			remaining -= getDays(convertToDays(temp));
		}
		if(convertToHours(remaining) < 24 && showHours) {
			append = append + "" + convertToHours(remaining) + " hour"+(convertToHours(remaining) == 1 ? "" : "s")+" ";
			long temp = remaining;
			remaining -= getHours(convertToHours(temp));
		}
		if(convertToMinutes(remaining) < 60 && showMinutes) {
			append = append + "" + convertToMinutes(remaining) + " minute"+(convertToMinutes(remaining) == 1 ? "" : "s")+" ";
			long temp = remaining;
			remaining -= getMinutes(convertToMinutes(temp));
		}
		if(convertToSeconds(remaining) < 60 && showSeconds) {
			append = append + "" + convertToSeconds(remaining) + " second"+(convertToSeconds(remaining) == 1 ? "" : "s")+"";
			long temp = remaining;
			remaining -= getSeconds(convertToSeconds(temp));
		}
		return append;
	}
	
	/**
	 * Returns a formatted string that includes days, hours, minutes and seconds according
	 * to what the @param time variable's value is.
	 * @param time
	 * @return
	 */
	public static String formatCountdown(long time, boolean showHours, boolean showMinutes, boolean showSeconds) {
		return formatCountdown(time, true, showHours, showMinutes, showSeconds);
	}
	
	/**
	 * Returns a formatted string that includes days, hours, minutes and seconds according
	 * to what the @param time variable's value is.
	 * @param time
	 * @return
	 */
	public static String formatCountdown(long time, boolean showMinutes, boolean showSeconds) {
		return formatCountdown(time, true, true, showMinutes, showSeconds);
	}
	
	/**
	 * Returns a formatted string that includes days, hours, minutes and seconds according
	 * to what the @param time variable's value is.
	 * @param time
	 * @return
	 */
	public static String formatCountdown(long time, boolean showSeconds) {
		return formatCountdown(time, true, true, true, showSeconds);
	}
	
	/**
	 * Returns a formatted string that includes days, hours, minutes and seconds according
	 * to what the @param time variable's value is.
	 * @param time
	 * @return
	 */
	public static String formatCountdown(long time) {
		return formatCountdown(time, true, true, true, true);
	}
	
}
