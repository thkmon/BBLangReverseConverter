package com.bb.filefixer.data.lang;

import java.io.File;

import com.bb.filefixer.data.file.BBFileContent;
import com.bb.filefixer.data.text.StringList;
import com.bb.filefixer.data.text.StringListMap;
import com.bb.filefixer.data.text.StringMap;
import com.bb.filefixer.util.file.BBFileReadUtil;

public class PropFile {
	
	private boolean bSuccess = false;
	private StringMap keyMap = null;
	private StringListMap valueMap = null;
	private StringList keyList = null;
	private StringList valueList = null;
	private BBFileContent fileContent = null;
	
	
	public boolean isbSuccess() {
		return bSuccess;
	}


	public void setbSuccess(boolean bSuccess) {
		this.bSuccess = bSuccess;
	}


	public StringMap getKeyMap() {
		return keyMap;
	}


	public void setKeyMap(StringMap keyMap) {
		this.keyMap = keyMap;
	}


	public StringListMap getValueMap() {
		return valueMap;
	}


	public void setValueMap(StringListMap valueMap) {
		this.valueMap = valueMap;
	}


	public StringList getKeyList() {
		return keyList;
	}


	public void setKeyList(StringList keyList) {
		this.keyList = keyList;
	}


	public StringList getValueList() {
		return valueList;
	}


	public void setValueList(StringList valueList) {
		this.valueList = valueList;
	}


	public BBFileContent getFileContent() {
		return fileContent;
	}


	public void setFileContent(BBFileContent fileContent) {
		this.fileContent = fileContent;
	}


	public PropFile(String langFilePath, String encode) throws Exception {
		this.fileContent = BBFileReadUtil.readFile(langFilePath, encode);
		if (fileContent == null || fileContent.size() == 0) {
			return;
		}
		
		this.keyMap = new StringMap();
		this.valueMap = new StringListMap();
		this.keyList = new StringList();
		this.valueList = new StringList();
		
		String singleLine = null;
		int lineCount = fileContent.size();
		for (int i=0; i<lineCount; i++) {
			singleLine = fileContent.get(i);
			if (singleLine == null || singleLine.length() == 0) {
				continue;
			}
			
			// ignore comments
			if (singleLine.trim().startsWith("#")) {
				continue;
			}
			
			int equalIndex = singleLine.indexOf("=");
			String key = singleLine.substring(0, equalIndex).trim();
			String value = singleLine.substring(equalIndex + 1).trim();
			
			if (key.length() == 0) {
				continue;
			}
			
			if (value.length() == 0) {
				continue;
			}
			
			if (keyMap.get(key) == null) {
				keyList.add(key);
				keyMap.put(key, value);

				if (valueMap.get(value) == null) {
					valueList.add(value);
				}
				valueMap.putString(value, key);
				
			} else {
				System.err.println("BBLangFile : Duplication ERROR. key == [" + key + "]");
				System.err.println(key + " : " + keyMap.get(key));
				System.err.println(key + " : " + value);
				System.err.println("");
			}
		}
		
		bSuccess = true;
	}
	
	
	public File getFile() {
		
		BBFileContent fileContent = this.getFileContent();
		if (fileContent != null) {
			File file = fileContent.getFile();
			if (file != null) {
				return file;
			}
		}
		
		return null;
	}
	
	
	public String getFilePath() {
		
		File file = getFile();
		if (file != null) {
			String absolutePath = file.getAbsolutePath();
			if (absolutePath != null && absolutePath.length() > 0) {
				return absolutePath;
			}
		}
		
		return "";
	}
	
	
	public boolean fixValueOfFileContent(String key, String newValue) {
		return fixValueOfFileContent(new StringList(key), newValue);
	}
	
	
	public boolean fixValueOfFileContent(StringList keyList, String newValue) {
		if (keyList == null || keyList.size() == 0) {
			return false;
		}
		
		if (this.getFileContent() == null || this.getFileContent().size() == 0) {
			return false;
		}
		
		int keyCount = keyList.size();
		
		String singleLine = null;
		int lineCount = this.getFileContent().size();
		for (int i=0; i<lineCount; i++) {
			singleLine = this.getFileContent().get(i);
			if (singleLine == null || singleLine.length() == 0) {
				continue;
			}
			
			if (singleLine.trim().startsWith("#")) {
				continue;
			}
			
			int equalIndex = singleLine.indexOf("=");
			if (equalIndex < 0) {
				continue;
			}
			
			String oldKey = singleLine.substring(0, equalIndex).trim();
			// String oldValue = singleLine.substring(equalIndex + 1).trim();
			
			if (oldKey.length() == 0) {
				continue;
			}
			
			for (int k=0; k<keyCount; k++) {
				if (keyList.get(k) == null || keyList.get(k).length() == 0) {
					continue;
				}
				
				if (keyList.get(k).equals(oldKey)) {
					this.getFileContent().set(i, oldKey + "=" + newValue);
					break;
				}
			}
		}
		
		return true;
	}
}











