package com.bb.filefixer.data.text;

import java.util.HashMap;

public class StringListMap extends HashMap<String, StringList> {

	
	public void putString(String key, String value) {
		if (this.get(key) != null) {
			this.get(key).add(value);
			
		} else {
			this.put(key, new StringList(value));
		}
	}
}
