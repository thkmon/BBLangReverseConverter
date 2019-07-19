package com.bb.filefixer.util.str;

import com.bb.filefixer.data.text.StringList;

public class StringUtil {

	public static String emptyToDefault(String str, String defaultValue) {
		if (str == null || str.length() == 0 || str.trim().length() == 0) {
			return defaultValue;
		}
		
		return str;
	}
	
	
	public static String parseString(Object obj) {
		if (obj == null) {
			return "";
		}
		
		String result = "";
		try {
			result = String.valueOf(obj);
			
		} catch (Exception e) {
			return "";
		}
		
		return result;
	}
	
	
	public static StringList splitWithTrim(String str, String delimiter) {
		StringList resultList = null;
		
		if (str == null || str.trim().length() == 0) {
			return null;
			
		} else {
			str = str.trim();
		}
		
		if (str.indexOf(delimiter) < 0) {
			resultList = new StringList();
			resultList.add(str);
			return resultList;
		}
		
		String[] arr = str.split(delimiter);
		if (arr == null || arr.length == 0) {
			return null;
		}
		
		resultList = new StringList();
		
		int arrLen = arr.length;
		for (int i=0; i<arrLen; i++) {
			resultList.add(arr[i].trim());
		}
		
		return resultList;
	}
}