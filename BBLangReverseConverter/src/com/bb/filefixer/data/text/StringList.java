package com.bb.filefixer.data.text;

import java.util.ArrayList;

public class StringList extends ArrayList<String> {
	
	
	public StringList() {
		super();
	}
	
	
	public StringList(String... str) {
		super();
		
		if (str != null && str.length > 0) {
			int count = str.length;
			for (int i=0; i<count; i++) {
				this.add(str[i]);
			}
		}
	}
	
	
	/**
	 * 대소문자 무시하고 찾는다.
	 * 
	 * @param strToFind
	 * @return
	 */
	public boolean findIgnoreCase(String strToFind) {
		if (strToFind == null || strToFind.length() == 0) {
			return false;
		}
		
		String oneStr = "";
		int count = this.size();
		for (int i=0; i<count; i++) {
			oneStr = this.get(i);
			if (oneStr == null || oneStr.length() == 0) {
				continue;
			}
			
			if (oneStr.equalsIgnoreCase(strToFind)) {
				return true;
			}
		}
		
		return false;
	}
}