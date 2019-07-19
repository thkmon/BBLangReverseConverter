package com.bb.filefixer.util.str;

import java.util.Calendar;

public class DateUtil {
	
	
	/**
	 * 현재날짜를 "yyyyMMddHHmmss" 형식의 String 형태로 리턴한다.
	 * @return
	 */
	public static String getTodayDateTime() {
		Calendar cal = Calendar.getInstance();
		StringBuffer today = new StringBuffer();
		today.append(String.format("%04d", cal.get(Calendar.YEAR)));
		today.append(String.format("%02d", cal.get(Calendar.MONTH) + 1));
		today.append(String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)));
		today.append(String.format("%02d", cal.get(Calendar.HOUR_OF_DAY)));
		today.append(String.format("%02d", cal.get(Calendar.MINUTE)));
		today.append(String.format("%02d", cal.get(Calendar.SECOND)));
		return today.toString();
	}
	
	
	/**
	 * 현재날짜를 "yyyy/MM/dd HH:mm:ss" 형식의 String 형태로 리턴한다.
	 * @return
	 */
	public static String getTodayDateTimeStd() {
		Calendar cal = Calendar.getInstance();
		StringBuffer today = new StringBuffer();
		today.append(String.format("%04d", cal.get(Calendar.YEAR)));
		today.append("/");
		today.append(String.format("%02d", cal.get(Calendar.MONTH) + 1));
		today.append("/");
		today.append(String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)));
		today.append(" ");
		today.append(String.format("%02d", cal.get(Calendar.HOUR_OF_DAY)));
		today.append(":");
		today.append(String.format("%02d", cal.get(Calendar.MINUTE)));
		today.append(":");
		today.append(String.format("%02d", cal.get(Calendar.SECOND)));
		return today.toString();
	}
}