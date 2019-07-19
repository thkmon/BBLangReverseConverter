package com.bb.filefixer.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.bb.filefixer.util.str.DateUtil;


public class FileLogManager {
	
	private FileOutputStream fileOutputStream = null;
	private OutputStreamWriter outputStreamWriter = null;
	private BufferedWriter bufferedWriter = null;
	
	public FileLogManager(String name) {
		if (name == null) {
			name = "log";
		} else {
			name = name.trim();
		}
		
		String dateTimeStd = DateUtil.getTodayDateTimeStd();
		String dateTime = dateTimeStd.replace("/", "").replace(":", "").replace(" ", "");
		
		// 로그파일 열기
		openLogFile(name + "_" + dateTime + ".txt");
		
		this.debug("----------");
		this.debug("기록 시간 : " + dateTimeStd);
		this.debug("----------");
	}
	
	
	/**
	 * 로그 파일 열기
	 * @param fileName
	 */
	private void openLogFile(String fileName) {
		boolean bError = false;
		
		try {
			File logDir = new File("log");
			if (!logDir.exists()) {
				logDir.mkdirs();
			}
			
			File file = new File("log\\" + fileName);
			boolean bAppend = true;
			
			fileOutputStream = new FileOutputStream(file, bAppend);
			outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
			bufferedWriter = new BufferedWriter(outputStreamWriter);
		
		} catch (IOException e) {
			bError = true;
			e.printStackTrace();
			
		} catch (Exception e) {
			bError = true;
			e.printStackTrace();
			
		} finally {
			if (bError) {
				close(bufferedWriter);
				close(outputStreamWriter);
				close(fileOutputStream);
			}
		}
	}
	
	
	/**
	 * 로그 파일 닫기
	 */
	public void closeLogFile() {
		close(bufferedWriter);
		close(outputStreamWriter);
		close(fileOutputStream);
	}
	
	
	/**
	 * 로그 파일 쓰기
	 * 
	 * @param oneLine
	 * @param bAppend
	 * @return
	 */
	private boolean writeLogFile(String oneLine, boolean bAppend) {
		
		boolean bError = false;
		boolean bWrite = false;

		try {
			if (bufferedWriter != null) {
				bufferedWriter.write(oneLine, 0, oneLine.length());
				bufferedWriter.newLine();
				
			} else {
				System.err.println("FileLogManager writeLogFile : bufferedWriter is null");
			}

			bWrite = true;

		} catch (IOException e) {
			bError = true;
			e.printStackTrace();

		} catch (Exception e) {
			bError = true;
			e.printStackTrace();

		} finally {
			if (bError) {
				close(bufferedWriter);
				close(outputStreamWriter);
				close(fileOutputStream);
			}
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
			
		} finally {
			bufferedWriter = null;
		}
	}

	
	private static void close(OutputStreamWriter outputStreamWriter) {
		try {
			if (outputStreamWriter != null) {
				outputStreamWriter.close();
			}
		} catch (Exception e) {
			// 무시
			
		} finally {
			outputStreamWriter = null;
		}
	}

	
	private static void close(FileOutputStream fileOutputStream) {
		try {
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		} catch (Exception e) {
			// 무시
			
		} finally {
			fileOutputStream = null;
		}
	}
	
	
	public void debug(String str) {
		writeLogFile(str, true);
		System.out.println(str);
	}
	
	
	public void debug(Object obj) {
		
		if (obj == null) {
			debug("null");
			return;
		}
		
		String str = "";
		
		try {
			str = String.valueOf(obj);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		debug(str);
	}
}