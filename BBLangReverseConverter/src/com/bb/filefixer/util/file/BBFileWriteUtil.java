package com.bb.filefixer.util.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class BBFileWriteUtil {
	
	
	public static boolean writeFile(String filePath, ArrayList<String> stringList, String encode, boolean bAppend) throws Exception {
		if (filePath == null || filePath.length() == 0) {
			return false;
		}

		boolean bWrite = false;

		FileOutputStream fileOutputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		BufferedWriter bufferedWriter = null;

		try {
			File file = new File(filePath);
			fileOutputStream = new FileOutputStream(file, bAppend);
			outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
			bufferedWriter = new BufferedWriter(outputStreamWriter);

			if (stringList != null && stringList.size() > 0) {
				String oneLine = null;

				int lineCount = stringList.size();
				int lastIndex = lineCount - 1;

				for (int i = 0; i < lineCount; i++) {
					oneLine = stringList.get(i);

					bufferedWriter.write(oneLine, 0, oneLine.length());
					if (i < lastIndex) {
						bufferedWriter.newLine();
					}
				}
			}

			bWrite = true;

		} catch (IOException e) {
			throw e;

		} catch (Exception e) {
			throw e;

		} finally {
			close(bufferedWriter);
			close(outputStreamWriter);
			close(fileOutputStream);
		}

		return bWrite;
	}
	
	
	public static void close(BufferedWriter obj) {
		try {
			if (obj != null) {
				obj.close();
			}
		} catch (Exception e) {
			
		} finally {
			obj = null;
		}
	}
	
	
	public static void close(OutputStreamWriter obj) {
		try {
			if (obj != null) {
				obj.close();
			}
		} catch (Exception e) {
			
		} finally {
			obj = null;
		}
	}
	
	
	public static void close(FileOutputStream obj) {
		try {
			if (obj != null) {
				obj.close();
			}
		} catch (Exception e) {
			
		} finally {
			obj = null;
		}
	}
}