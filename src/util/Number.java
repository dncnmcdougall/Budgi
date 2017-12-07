package util;

import java.util.Calendar;

public class Number {

	/**
	 * Rounds the given number to two decimal places
	 * 
	 * @param in
	 *            The number to round off
	 * @return The rounded off number
	 */
	public static double round2Dp(double in) {
		return round(in, 2);
	}

	/**
	 * Rounds the given number to 'dp' decimal places
	 * 
	 * @param in
	 *            The number to round off
	 * @param dp
	 *            The number of decimal places
	 * @return The rounded off number
	 */

	public static double round(double in, int dp) {
		double i = Math.pow(10, dp);
		return Math.round(in * i) / i;
	}
	
	/**
	 * Returns how far we are through the month as a number form 0 to 1.
	 * 
	 * @return The progress through the month.
	 */
	public static double getPecInMonth(){
		Calendar c =Calendar.getInstance();
		double d =c.get(Calendar.DAY_OF_MONTH);
		double md =c.getActualMaximum(Calendar.DAY_OF_MONTH);
		return d/md;
	}
}
