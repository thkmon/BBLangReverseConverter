package com.bb.filefixer.data.file;

import java.io.File;

import com.bb.filefixer.data.text.StringList;

public class BBFileContent extends StringList {
	
	
	private File file = null;
	
	
	public BBFileContent(File file) {
		this.file = file;
	}


	public File getFile() {
		return file;
	}	
}