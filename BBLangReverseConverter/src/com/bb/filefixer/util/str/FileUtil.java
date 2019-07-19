package com.bb.filefixer.util.str;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.bb.filefixer.data.text.StringList;

public class FileUtil {
	public static StringList readFile(File file) {
		if (file == null || !file.exists()) {
			return null;
		}

		StringList resultList = null;

		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileInputStream = new FileInputStream(file);
			inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);

			String oneLine = null;
			while ((oneLine = bufferedReader.readLine()) != null) {
				if (resultList == null) {
					resultList = new StringList();
				}

				resultList.add(oneLine);
			}

		} catch (IOException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			close(bufferedReader);
			close(inputStreamReader);
			close(fileInputStream);
		}

		return resultList;
	}

	
	public static boolean writeFile(String filePath, StringList stringList, boolean bAppend) {
		if (filePath == null || filePath.length() == 0) {
			return false;
		}

		File file = new File(filePath);

		boolean bWrite = false;

		FileOutputStream fileOutputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		BufferedWriter bufferedWriter = null;

		try {
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
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			close(bufferedWriter);
			close(outputStreamWriter);
			close(fileOutputStream);
		}

		return bWrite;
	}
	
	
	private static void close(BufferedWriter bufferedWriter) {
		try {
			if (bufferedWriter != null) {
				bufferedWriter.close();
			}
		} catch (Exception e) {
			// 무시
		}
	}

	private static void close(OutputStreamWriter outputStreamWriter) {
		try {
			if (outputStreamWriter != null) {
				outputStreamWriter.close();
			}
		} catch (Exception e) {
			// 무시
		}
	}

	private static void close(FileOutputStream fileOutputStream) {
		try {
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		} catch (Exception e) {
			// 무시
		}
	}

	private static void close(FileInputStream fileInputStream) {
		try {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
		} catch (Exception e) {
			// 무시
		}
	}

	private static void close(InputStreamReader inputStreamReader) {
		try {
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
		} catch (Exception e) {
			// 무시
		}
	}

	private static void close(BufferedReader bufferedReader) {

		try {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		} catch (Exception e) {
			// 무시
		}
	}
	
	
	public static String getFileExtension(String filePath) {
		if (filePath == null || filePath.length() == 0) {
			return "";
		}
		
		int lastSlashIdx = filePath.lastIndexOf("/");
		int lastBackSlashIdx = filePath.lastIndexOf("\\");
		if (lastSlashIdx > -1 || lastBackSlashIdx > -1) {
			if (lastSlashIdx > lastBackSlashIdx) {
				filePath = filePath.substring(lastSlashIdx + 1);
			} else {
				filePath = filePath.substring(lastBackSlashIdx + 1);
			}
		}
		
		int lastDotIdx = filePath.lastIndexOf(".");
		if (lastDotIdx > -1) {
			return filePath.substring(lastDotIdx  + 1);
		}
		
		return "";
	}
	
	
	public static int findLineByReadFile(File file, String strToFind, boolean bIgnoreCase) {
		if (file == null || !file.exists()) {
			return -1;
		}
		
		if (!file.isFile()) {
			return -1;
		}
		
		int lineNumber = -1;

		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;

		try {
			fileInputStream = new FileInputStream(file);
			inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);

			if (bIgnoreCase) {
				strToFind = strToFind.toLowerCase();
			}
			
			int lineCount = 0;
			int foundIndex = 0;
			
			String oneLine = null;
			while ((oneLine = bufferedReader.readLine()) != null) {
				lineCount++;
				
				if (bIgnoreCase) {
					foundIndex = oneLine.toLowerCase().indexOf(strToFind);
				} else {
					foundIndex = oneLine.indexOf(strToFind);
				}
				
				if (foundIndex > -1) {
					lineNumber = lineCount;
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			close(bufferedReader);
			close(inputStreamReader);
			close(fileInputStream);
		}

		return lineNumber;
	}
}